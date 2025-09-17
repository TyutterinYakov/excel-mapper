package ru.excel.converter.reader.customization;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.excel.converter.annotation.ExcelCell;

import java.lang.reflect.Field;
import java.util.Optional;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReaderCustomization {
    private Field currentField;
    private Optional<String> dateFormat = Optional.empty();

    public static ReaderCustomization of(Field field) {
        final ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
        return ReaderCustomization
                .builder()
                .currentField(field)
                .dateFormat(extractDateFormat(excelCell))
                .build();
    }

    private static Optional<String> extractDateFormat(ExcelCell excelCell) {
        if (!StringUtils.isBlank(excelCell.dateFormat())) {
            return Optional.of(excelCell.dateFormat());
        }
        return Optional.empty();
    }
}
