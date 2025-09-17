package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.CellType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

import java.math.BigDecimal;

@Component
public class LongReader extends NumberExcelReader<Long> {

    public LongReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Long read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return catcher(cell, Long.class, Long::parseLong, BigDecimal::longValueExact);
    }
}
