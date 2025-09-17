package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;

@Component
public class ByteReader extends NumberExcelReader<Byte> {

    public ByteReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Byte read(@NotNull Cell cell) {
        return catcher(cell.getRawValue(), Byte.class, () -> Byte.parseByte(cell.getRawValue().trim()));
    }
}
