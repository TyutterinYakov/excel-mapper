package ru.excel.converter.reader;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import ru.excel.converter.exception.CellExcelReaderException;

import java.util.Locale;
import java.util.function.Supplier;

public abstract class NumberExcelReader<T extends Number> extends ExcelReader<T> {
    private static final String MESSAGE_KEY = "reader.number.incorrectValue";

    public NumberExcelReader(MessageSource messageSource) {
        super(messageSource);
    }

    protected T catcher(@NotNull String value, @NotNull Class<T> type, @NotNull Supplier<T> catcher) {
        try {
            return catcher.get();
        } catch (NumberFormatException ex) {
            throw new CellExcelReaderException(messageSource.getMessage(MESSAGE_KEY,
                    new Object[]{value, type.getName()}, Locale.getDefault()));
        }
    }
}
