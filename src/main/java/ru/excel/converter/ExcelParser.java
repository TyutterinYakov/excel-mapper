package ru.excel.converter;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.excel.converter.annotation.ExcelCell;
import ru.excel.converter.exception.CellExcelReaderException;
import ru.excel.converter.exception.CellParsingException;
import ru.excel.converter.reader.ExcelReader;
import ru.excel.converter.reader.ExcelReaderFactory;
import ru.excel.converter.util.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.excel.converter.util.ColumnNamesValidator.validate;
import static ru.excel.converter.util.ReflectionUtil.newInstance;
import static ru.excel.converter.util.ReflectionUtil.setValueToField;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelParser {
    private final ExcelReaderFactory excelReaderFactory;

    //TODO Добавить возможность кастомизации путем передачи какого-то класса на вход
    //TODO Добавить возможность получить на выходе stream

    /**
     * Парсит excel файл
     *
     * @param file        excel файл
     * @param type        тип, в который нужно смапить значения
     * @param sheetNumber номер листа с данными
     * @return Список полученных объектов
     */
    public <T> @NotNull List<T> parse(@NotNull File file, @NotNull Class<T> type, int sheetNumber) {
        try (ReadableWorkbook readableWorkbook = new ReadableWorkbook(file)) {
            return process(readableWorkbook, type, sheetNumber).toList();
        } catch (IOException e) {
            throw new IllegalStateException("Error when reading an excel file", e);
        }
    }

    /**
     * Парсит excel файл
     *
     * @param is          поток
     * @param type        тип, в который нужно смапить значения
     * @param sheetNumber номер листа с данными
     * @return Список полученных объектов
     */
    public <T> @NotNull List<T> parse(@NotNull InputStream is, @NotNull Class<T> type, int sheetNumber) {
        try (ReadableWorkbook readableWorkbook = new ReadableWorkbook(is)) {
            return process(readableWorkbook, type, sheetNumber).toList();
        } catch (IOException e) {
            throw new IllegalStateException("Error when reading an excel file", e);
        }
    }

    private <T> @NotNull Stream<T> process(@NotNull ReadableWorkbook readableWorkbook,
                                           @NotNull Class<T> type, int sheetNumber) {
        final Sheet sheet = readableWorkbook.getSheet(sheetNumber).orElseThrow(() ->
                new IllegalStateException("The sheet with index " + sheetNumber +
                        " is missing from the transferred file"));

        final Map<String, Field> fieldByColumnName = ReflectionUtil.getDeclaredFieldsWithAnnotation(type, ExcelCell.class)
                .stream().collect(Collectors.toMap(f -> f.getAnnotation(ExcelCell.class).name(), Function.identity()));
        try {
//            final AtomicReference<CellParsingException> cellParsingException = new AtomicReference<>(new CellParsingException());
            Map<Integer, Map<Integer, Map<String, List<String>>>> errors = new ConcurrentHashMap<>();
            final AtomicInteger rowIndex = new AtomicInteger(0);
            final List<String> columnNames = getColumnNames(sheet, type);
            if (columnNames.isEmpty()) {
                log.debug("First line from file is empty. Returning empty stream");
                return Stream.empty();
            }

            //skip(1) - skip the first line, as it contains the names of the columns
            final Stream<T> streamResult = sheet.openStream().skip(1).map(row -> {
                final int currentRowIndex = rowIndex.getAndIncrement();
                final T result = newInstance(type);
                int colNumber = 0;
                int setValueCount = 0; //Count for counting setting values to fields
                for (Cell cell : row) {
                    if (cell != null && !StringUtils.isBlank(cell.getRawValue())) {
                        final String columnName = columnNames.get(colNumber);
                        final Field field = fieldByColumnName.get(columnName);
                        final ExcelReader<?> reader = excelReaderFactory
                                .getReader(field);
                        try {
                            final Object readValue = reader.read(cell);
                            setValueToField(field, readValue, result);
                        } catch (CellExcelReaderException ex) {
                            final Map<Integer, Map<String, List<String>>> rowErrors = errors.computeIfAbsent(currentRowIndex, v -> new HashMap<>());
                            final Map<String, List<String>> columnError = rowErrors.computeIfAbsent(colNumber, v -> new HashMap<>());
                            final List<String> cellError = columnError.computeIfAbsent(columnName, v -> new ArrayList<>());
                            cellError.add(ex.getMessage());
//                            cellParsingException.get().addError(colNumber, currentRowIndex, columnName, ex.getMessage());
                        }
                        setValueCount++;
                    }
                    colNumber++;
                }
                //Skip empty line
                if (setValueCount != 0) {
                    return result;
                }
                return null;
            }).filter(Objects::nonNull);

            if (!errors.isEmpty()) {
                throw new CellParsingException(errors);
            }
            return streamResult;
        } catch (IOException e) {
            throw new IllegalStateException("Error when reading excel sheet", e);
        }
    }

    private @NotNull List<String> getColumnNames(@NotNull Sheet sheet, Class<?> type) throws IOException {
        final Stream<Row> row = sheet.openStream();
        final Row cells = row.findFirst().orElseThrow(() ->
                new IllegalStateException("The first line with the column names is missing in the file"));
        final List<String> columnNames = cells.stream().map(Cell::getText).toList();
        return validate(columnNames, type);
    }
}
