package ru.excel.converter.reader;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import ru.excel.converter.exception.CellExcelReaderException;

import java.util.Locale;
import java.util.function.Supplier;

@Slf4j
public abstract class NumberExcelReader<T extends Number> extends ExcelReader<T> {
    private static final String MESSAGE_KEY = "reader.number.incorrectValue";

    public NumberExcelReader(MessageSource messageSource) {
        super(messageSource);
    }

    protected T catcher(@NotNull String value, @NotNull Class<T> type, @NotNull Supplier<T> catcher) {
        try {
            return catcher.get();
        } catch (Exception ex) {
            log.warn("Incorrect value {} for type {}", value, type.getName());
            throw new CellExcelReaderException(messageSource.getMessage(MESSAGE_KEY,
                    new Object[]{value, type.getName()}, Locale.getDefault()));
        }
    }
}
