package ru.excel.converter.classes;

import ru.excel.converter.annotation.ExcelCell;

public class NoRequiredAllColumns {
    @ExcelCell(name = "column_str", required = false)
    private Object columnVal;
    @ExcelCell(name = "another_column", required = false)
    private String anotherColumn;
}
