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
public class WithAllDefaultJavaTypes {
    @ExcelCell(name = "byte_value")
    private Byte byteValue;
    @ExcelCell(name = "int_value")
    private Integer intValue;
    @ExcelCell(name = "long_value")
    private Long longValue;
    //TODO short
    @ExcelCell(name = "float_value")
    private Float floatValue;
    @ExcelCell(name = "double_value")
    private Double doubleValue;

    @ExcelCell(name = "str_value")
    private String strValue;
    @ExcelCell(name = "local_date_value")
    private LocalDate localDateValue;
    @ExcelCell(name = "local_date_time_value")
    private LocalDateTime localDateTimeValue;
    @ExcelCell(name = "instant_value")
    private Instant instantValue;
    @ExcelCell(name = "offset_date_time_value")
    private OffsetDateTime offsetDateTimeValue;
    @ExcelCell(name = "bool_value")
    private Boolean boolValue;
}
