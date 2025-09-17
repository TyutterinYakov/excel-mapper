package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.NumberExcelReader;

@Component
public class LongReader extends NumberExcelReader<Long> {

    public LongReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Long read(@NotNull Cell cell) {
        return catcher(cell.getText(), Long.class, () -> Long.parseLong(cell.getText().trim()));
    }
}
