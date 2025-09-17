package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.DateExcelReader;

import java.time.LocalDate;

@Component
public class LocalDateReader extends DateExcelReader<LocalDate> {

    public LocalDateReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull LocalDate read(@NotNull Cell cell) {
        return catcher(cell.getText(), LocalDate.class, () -> LocalDate.parse(cell.getText().trim()));
    }
}
