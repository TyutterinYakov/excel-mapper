package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;

@Component
public class DoubleReader extends NumberExcelReader<Double> {
    public DoubleReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Double read(@NotNull Cell cell) {
        return catcher(cell.getRawValue(), Double.class, () -> Double.parseDouble(cell.getRawValue().trim()));
    }
}
