package ru.excel.converter.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.excel.converter.annotation.ExcelCell;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlyDateTypes {
    @ExcelCell(name = "local_date_value")
    private LocalDate localDateValue;
    @ExcelCell(name = "local_date_time_value")
    private LocalDateTime localDateTimeValue;
    @ExcelCell(name = "instant_value")
    private Instant instantValue;
    @ExcelCell(name = "offset_date_time_value")
    private OffsetDateTime offsetDateTimeValue;
    @ExcelCell(name = "custom_local_date_value_format", dateFormat = "MM/dd/yyyy")
    private LocalDate customLocalDateValueFormat;
}
