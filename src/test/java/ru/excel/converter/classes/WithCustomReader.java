package ru.excel.converter.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.excel.converter.annotation.ExcelCell;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithCustomReader {

    @ExcelCell(name = "custom_excel_reader_value", customCellReader = CustomReader.class)
    private String customExcelReaderValue;
    @ExcelCell(name = "str_value")
    private String strValue;
}
