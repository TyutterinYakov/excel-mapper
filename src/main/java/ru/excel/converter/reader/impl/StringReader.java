package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.ExcelReader;

@Component
class StringReader extends ExcelReader<String> {

    public StringReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull String read(@NotNull Cell cell) {
        return cell.getRawValue().trim();
    }
}
