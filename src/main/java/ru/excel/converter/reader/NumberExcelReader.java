package ru.excel.converter.reader;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.CellType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import ru.excel.converter.exception.CellExcelReaderException;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.function.Function;

@Slf4j
public abstract class NumberExcelReader<T extends Number> extends ExcelReader<T> {
    private static final String MESSAGE_KEY = "reader.number.incorrectValue";

    public NumberExcelReader(MessageSource messageSource) {
        super(messageSource);
    }

    protected T catcher(@NotNull Cell cell, @NotNull Class<T> type,
                        @NotNull Function<String, T> forStrValue,
                        @NotNull Function<BigDecimal, T> forNumberType) {
        try {
            if (cell.getType() == CellType.NUMBER) {
                return forNumberType.apply(cell.asNumber());
            }
            return forStrValue.apply(cell.getRawValue().trim());
        } catch (Exception ex) {
            log.debug("Incorrect value {} for type {}", cell.getText(), type.getName());
            throw new CellExcelReaderException(messageSource.getMessage(MESSAGE_KEY,
                    new Object[]{cell.getText(), type.getName()}, Locale.getDefault()));
        }
    }
}
