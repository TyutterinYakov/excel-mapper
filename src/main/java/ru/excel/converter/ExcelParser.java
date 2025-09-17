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
import ru.excel.converter.exception.ExcelException;
import ru.excel.converter.exception.ExcelParsingException;
import ru.excel.converter.reader.ExcelReader;
import ru.excel.converter.reader.ExcelReaderFactory;
import ru.excel.converter.reader.customization.ReaderCustomization;
import ru.excel.converter.util.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
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
            return process(readableWorkbook, type, sheetNumber);
        } catch (IOException e) {
            throw new ExcelException("Error when reading an excel file", e);
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
            return process(readableWorkbook, type, sheetNumber);
        } catch (IOException e) {
            throw new ExcelException("Error when reading an excel file", e);
        }
    }

    private <T> @NotNull List<T> process(@NotNull ReadableWorkbook readableWorkbook,
                                           @NotNull Class<T> type, int sheetNumber) {
        final Sheet sheet = readableWorkbook.getSheet(sheetNumber).orElseThrow(() ->
                new ExcelException("The sheet with index " + sheetNumber +
                        " is missing from the transferred file"));

        final Map<String, ReaderCustomization> readerCustomizationByColumnName = ReflectionUtil.getDeclaredFieldsWithAnnotation(type, ExcelCell.class)
                .stream().collect(Collectors.toMap(f -> f.getAnnotation(ExcelCell.class).name(), ReaderCustomization::of));
        try {
            final ExcelParsingException exception = new ExcelParsingException("An error occurred when parsing an Excel file");
            final int skipLinesCount = 1;
            final AtomicInteger rowIndex = new AtomicInteger(skipLinesCount);
            final List<String> columnNames = getColumnNames(sheet, type);
            if (columnNames.isEmpty()) {
                log.debug("First line from file is empty. Returning empty list");
                return List.of();
            }

            //skip(1) - skip the first line, as it contains the names of the columns
            final List<T> listResult = sheet.openStream().skip(skipLinesCount).map(row -> {
                final int currentRowIndex = rowIndex.getAndIncrement();
                final T result = newInstance(type);
                int colNumber = 0;
                int setValueCount = 0; //Count for counting setting values to fields
                for (Cell cell : row) {
                    if (cell != null && !StringUtils.isBlank(cell.getRawValue())) {
                        final String columnName = columnNames.get(colNumber);
                        final ReaderCustomization readerCustomization = readerCustomizationByColumnName.get(columnName);
                        final Field field = readerCustomization.getCurrentField();
                        final ExcelReader<?> reader = excelReaderFactory.getReader(field);
                        try {
                            final Object readValue = reader.read(cell, readerCustomization);
                            setValueToField(field, readValue, result);
                        } catch (CellExcelReaderException ex) {
                            log.debug("An error occurred when parsing the value", ex);
                            exception.addError(currentRowIndex, colNumber, columnName, ex.getMessage());
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
            }).filter(Objects::nonNull).toList();

            if (!exception.getErrors().isEmpty()) {
                log.warn(exception.getErrors().toString());
                throw exception;
            }
            return listResult;
        } catch (IOException e) {
            throw new ExcelException("Error when reading excel sheet", e);
        }
    }

    private @NotNull List<String> getColumnNames(@NotNull Sheet sheet, Class<?> type) throws IOException {
        final Stream<Row> row = sheet.openStream();
        final Row cells = row.findFirst().orElseThrow(() ->
                new ExcelException("The first line with the column names is missing in the file"));
        final List<String> columnNames = cells.stream().map(Cell::getText).toList();
        return validate(columnNames, type);
    }
}
