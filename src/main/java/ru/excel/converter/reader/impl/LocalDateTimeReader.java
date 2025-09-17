package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.CellType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.DateExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeReader extends DateExcelReader<LocalDateTime> {

    public LocalDateTimeReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull LocalDateTime read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return catcher(cell.getText(), LocalDateTime.class, () -> {
            final CellType type = cell.getType();
            //На случай, если пользователь задал кастомный формат даты в виде числа
            if (type == CellType.NUMBER || type == CellType.FORMULA) {
                return cell.asDate();
            }
            final String value = cell.getRawValue().trim();
            return readerCustomization.getDateFormat()
                    .map(pattern -> LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern)))
                    .orElseGet(() -> LocalDateTime.parse(value));
        });
    }
}
