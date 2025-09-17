package ru.excel.converter.annotation;

import ru.excel.converter.reader.CustomExcelReader;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ExcelCell {
    String name();
    boolean required() default true;
    String dateFormat() default "";
    Class<? extends CustomExcelReader> customCellReader() default CustomExcelReader.class;
}
