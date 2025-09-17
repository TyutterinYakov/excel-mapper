package ru.excel.converter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.excel.converter.annotation.ExcelCell;
import ru.excel.converter.exception.ExcelException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.excel.converter.util.ColumnNameFormatter.formatting;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnNamesValidator {

    public static @NotNull List<String> validate(@NotNull List<String> columnNamesFromFile, @NotNull Class<?> type) {
        final Set<String> columnNamesFromFileUnique = columnNamesFromFile.stream().map(ColumnNameFormatter::formatting)
                .collect(Collectors.toSet());
        if (columnNamesFromFileUnique.size() != columnNamesFromFile.size()) {
            throw new IllegalStateException("The columns in the file have a non-unique name. Columns: " + columnNamesFromFile);
        }
        final List<Field> declaredFieldsWithAnnotation = ReflectionUtil.getDeclaredFieldsWithAnnotation(type, ExcelCell.class);

        if (declaredFieldsWithAnnotation.isEmpty()) {
            throw new IllegalStateException("There are no fields annotated by @ExcelCell in the class with the " +
                    type.getName() + " type.");
        }

        final List<String> columnNameFromFields = new ArrayList<>(declaredFieldsWithAnnotation.size());
        for (Field field : declaredFieldsWithAnnotation) {
            final ExcelCell annotation = field.getAnnotation(ExcelCell.class);
            final String name = formatting(annotation.name());
            columnNameFromFields.add(name);
            if (!columnNamesFromFileUnique.contains(name) && annotation.required()) {
                //TODO: Возможно, подумать над более полный описанием ошибки. Чтобы выброс происходил после полного прохода
                throw new ExcelException("The required \"" + name + "\" column is missing from the file");
            }
        }

        if (new HashSet<>(columnNameFromFields).size() != columnNameFromFields.size()) {
            throw new IllegalStateException("There are duplicates among the titles announced in @ExcelCell: " +
                    columnNameFromFields);
        }

        columnNameFromFields.forEach(columnNamesFromFileUnique::remove);

        if (!columnNamesFromFileUnique.isEmpty()) {
            throw new ExcelException("The file contains unknown headers: " + columnNamesFromFileUnique);
        }

        return columnNamesFromFile;
    }
}
