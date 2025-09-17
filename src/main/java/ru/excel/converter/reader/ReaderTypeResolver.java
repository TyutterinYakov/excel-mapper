package ru.excel.converter.reader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import ru.excel.converter.annotation.ExcelCell;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReaderTypeResolver {

    /**
     * Возвращает нужный ExcelReader, подходящий для переданного поля
     *
     * @param field        поле класса, в которое нужно смапить значение
     * @param excelReaders классы для парсинга значения из ячейки
     * @return Соответствующий ExcelReader
     */
    public static @NotNull ExcelReader<?> resolve(@NotNull Field field,
                                                  @Nullable Map<Class<ExcelReader<?>>, ExcelReader<?>> excelReaders) {
        final Class<?> fieldType = field.getType();
        if (CollectionUtils.isEmpty(excelReaders)) {
            throw new IllegalStateException("There is no ExcelReader implementation for the " + fieldType.getName() + " type");
        }
        final Optional<Class<? extends CustomExcelReader>> customReaderType = extractExcelReader(field);
        if (customReaderType.isPresent()) {
            final ExcelReader<?> excelReader = excelReaders.get(customReaderType.get());
            if (excelReader == null) {
                throw new IllegalStateException("A Bean with the %s type is not registered in the context of Spring"
                        .formatted(customReaderType.get().getName()));
            }
            return excelReader;
        }
        final List<ExcelReader<?>> commonReader = excelReaders.values().stream().filter(r ->
                !CustomExcelReader.class.isAssignableFrom(r.getClass())).toList();

        if (commonReader.size() != 1) {
            throw new IllegalStateException("More than one ExcelReader is declared for the %s type. Found = %s"
                    .formatted(field.getType(), commonReader));
        }
        return commonReader.get(0);
    }

    private static Optional<Class<? extends CustomExcelReader>> extractExcelReader(@NotNull Field field) {
        final ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
        if (excelCell.customCellReader() != CustomExcelReader.class) {
            return Optional.of(excelCell.customCellReader());
        }
        return Optional.empty();
    }

}
