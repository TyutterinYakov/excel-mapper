package ru.excel.converter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnNameFormatter {
    public static @NotNull String formatting(@Nullable String columnName) {
        if (StringUtils.isBlank(columnName)) {
            throw new IllegalStateException("The column name cannot be empty or consist only of spaces");
        }
        return columnName.toLowerCase().trim();
    }
}
