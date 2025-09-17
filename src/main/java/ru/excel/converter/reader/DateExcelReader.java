package ru.excel.converter.reader;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import ru.excel.converter.exception.CellExcelReaderException;

import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Locale;
import java.util.function.Supplier;

public abstract class DateExcelReader<T extends Temporal> extends ExcelReader<T> {
    private static final String MESSAGE_KEY = "reader.date.incorrectValue";
    public DateExcelReader(MessageSource messageSource) {
        super(messageSource);
    }

    protected T catcher(@NotNull String value, @NotNull Class<T> type, @NotNull Supplier<T> catcher) {
        try {
            return catcher.get();
        } catch (DateTimeParseException ex) {
            throw new CellExcelReaderException(messageSource.getMessage(MESSAGE_KEY,
                    new Object[]{value, type.getName()}, Locale.getDefault()));
        }
    }
}
