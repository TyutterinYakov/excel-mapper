package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.exception.CellExcelReaderException;
import ru.excel.converter.reader.ExcelReader;

import java.util.Locale;
import java.util.Map;

@Component
class BooleanReader extends ExcelReader<Boolean> {
    private static final Map<String, Boolean> ACCEPTABLE_VALUES = Map.of(
            "1", true,
            "0", false,
            "true", true,
            "false", false
    );
    private static final String ERROR_MESSAGE_KEY = "reader.boolean.incorrectValue";

    public BooleanReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Boolean read(@NotNull Cell cell) {
        final Boolean result = ACCEPTABLE_VALUES.get(cell.getRawValue().trim().toLowerCase());
        if (result == null) {
            throw new CellExcelReaderException(messageSource.getMessage(ERROR_MESSAGE_KEY, new Object[]{cell.getRawValue()},
                    Locale.getDefault()));
        }
        return result;
    }
}
