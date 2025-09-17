package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.DateExcelReader;
import ru.excel.converter.reader.ExcelReader;

import java.time.Instant;
import java.time.OffsetDateTime;

@Component
public class OffsetDateTimeReader extends DateExcelReader<OffsetDateTime> {

    public OffsetDateTimeReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull OffsetDateTime read(@NotNull Cell cell) {
        return catcher(cell.getText(), OffsetDateTime.class, () -> OffsetDateTime.parse(cell.getText().trim()));
    }
}
