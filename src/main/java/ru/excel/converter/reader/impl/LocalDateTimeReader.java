package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.DateExcelReader;

import java.time.LocalDateTime;

@Component
public class LocalDateTimeReader extends DateExcelReader<LocalDateTime> {

    public LocalDateTimeReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull LocalDateTime read(@NotNull Cell cell) {
        return catcher(cell.getText(), LocalDateTime.class, () -> LocalDateTime.parse(cell.getText().trim()));
    }
}
