package ru.excel.converter.classes;

import ru.excel.converter.annotation.ExcelCell;

import java.time.OffsetDateTime;

public class CorrectColumnNames {

    @ExcelCell(name = "str_value")
    private String strValue;
    @ExcelCell(name = "int_value")
    private Integer intValue;
    @ExcelCell(name = "offset_date_time_value")
    private OffsetDateTime offsetDateTimeValue;
}
