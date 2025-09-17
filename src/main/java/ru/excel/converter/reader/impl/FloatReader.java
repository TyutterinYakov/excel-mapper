package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;

@Component
public class FloatReader extends NumberExcelReader<Float> {

    public FloatReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Float read(@NotNull Cell cell) {
        return catcher(cell.getRawValue(), Float.class, () -> Float.parseFloat(cell.getRawValue().trim()));
    }
}
