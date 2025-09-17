package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

import java.util.regex.Pattern;

@Component
public class IntegerReader extends NumberExcelReader<Integer> {
    private final String PATTERN = Pattern.compile("").pattern();

    public IntegerReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Integer read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return catcher(cell.getRawValue(), Integer.class, () -> Integer.parseInt(cell.getRawValue().trim()));
    }
}
