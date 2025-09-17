package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.CellType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

import java.math.BigDecimal;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

@Component
public class ByteReader extends NumberExcelReader<Byte> {

    public ByteReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Byte read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return catcher(cell, Byte.class, Byte::parseByte, BigDecimal::byteValueExact);
    }
}
