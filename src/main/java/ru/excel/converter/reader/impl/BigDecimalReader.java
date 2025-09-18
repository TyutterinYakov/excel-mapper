package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

import java.math.BigDecimal;
import java.util.function.Function;

@Component
public class BigDecimalReader extends NumberExcelReader<BigDecimal> {
    public BigDecimalReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull BigDecimal read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return super.catcher(cell, BigDecimal.class, BigDecimal::new, Function.identity());
    }
}
