package ru.excel.converter.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.excel.converter.annotation.ExcelCell;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreeTypesForMapping {

    @ExcelCell(name = "str_value")
    private String strValue;
    @ExcelCell(name = "long_value")
    private Long longValue;
    @ExcelCell(name = "bool_value")
    private Boolean boolValue;
}
