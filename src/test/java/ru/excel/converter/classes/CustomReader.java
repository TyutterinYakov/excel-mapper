package ru.excel.converter.classes;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.CustomExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

@Component
public class CustomReader extends CustomExcelReader<String> {
    public CustomReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull String read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return "THIS IS TEST CUSTOM READER VALUE";
    }
}
