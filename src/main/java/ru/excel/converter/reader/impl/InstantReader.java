package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.DateExcelReader;

import java.time.Instant;

@Component
public class InstantReader extends DateExcelReader<Instant> {
    public InstantReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull Instant read(@NotNull Cell cell) {
        return catcher(cell.getText(), Instant.class, () -> Instant.parse(cell.getText().trim()));
    }
}
