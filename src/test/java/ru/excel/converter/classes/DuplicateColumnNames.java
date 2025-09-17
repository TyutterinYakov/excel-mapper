package ru.excel.converter.classes;

import ru.excel.converter.annotation.ExcelCell;

public class DuplicateColumnNames {
    @ExcelCell(name = "column_str")
    private String columnStr;
    @ExcelCell(name = "column_str")
    private Object columnVal;
    @ExcelCell(name = "another_column")
    private String anotherColumn;
}
