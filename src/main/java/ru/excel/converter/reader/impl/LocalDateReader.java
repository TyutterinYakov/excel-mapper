package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.CellType;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.excel.converter.reader.DateExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateReader extends DateExcelReader<LocalDate> {

    public LocalDateReader(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public @NotNull LocalDate read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization) {
        return catcher(cell.getText(), LocalDate.class, () -> {
            final CellType type = cell.getType();
            //На случай, если пользователь задал кастомный формат даты в виде числа
            if (type == CellType.NUMBER || type == CellType.FORMULA) {
                return cell.asDate().toLocalDate();
            }
            final String value = cell.getText().trim();
            return readerCustomization.getDateFormat()
                    .map(pattern -> LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern)))
                    .orElseGet(() -> LocalDate.parse(value));
        });
    }
}
