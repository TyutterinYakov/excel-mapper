package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.DateExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OffsetDateTimeReader extends DateExcelReader<OffsetDateTime> {

    public OffsetDateTimeReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull OffsetDateTime read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return catcher(cell.getText(), OffsetDateTime.class, () -> {
            final String strValue = cell.getText().trim();
            return readerCustomization.getDateFormat()
                    .map(pattern -> OffsetDateTime.parse(strValue, DateTimeFormatter.ofPattern(pattern)))
                    .orElseGet(() -> OffsetDateTime.parse(strValue));
        });

    }
}
