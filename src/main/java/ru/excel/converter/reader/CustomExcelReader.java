package ru.excel.converter.reader;

import org.springframework.context.MessageSource;

public abstract class CustomExcelReader<T> extends ExcelReader<T> {
    public CustomExcelReader(MessageSource messageSource) {
        super(messageSource);
    }
}
