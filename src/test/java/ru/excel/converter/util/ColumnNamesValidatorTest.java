package ru.excel.converter.util;

import org.junit.jupiter.api.Test;
import ru.excel.converter.classes.CorrectColumnNames;
import ru.excel.converter.classes.DuplicateColumnNames;
import ru.excel.converter.classes.NoRequiredAllColumns;
import ru.excel.converter.classes.WithoutColumns;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ColumnNamesValidatorTest {

    @Test
    void validateTest() {
        final List<String> expected = List.of("str_value", "int_value", "offset_date_time_value");
        final List<String> actual = assertDoesNotThrow(() ->
                ColumnNamesValidator.validate(expected, CorrectColumnNames.class));
        assertEquals(expected, actual);
    }

    @Test
    void validateWithEmptyFirstLineInFileAndWithoutRequiredColumnsTest() {
        final List<String> columnsFromFile = List.of();
        List<String> columnNameFromFile = assertDoesNotThrow(() ->
                ColumnNamesValidator.validate(columnsFromFile, NoRequiredAllColumns.class));
        assertTrue(columnNameFromFile.isEmpty());
    }

    @Test
    void validateWithDuplicateColumnNamesInObjExceptionTest() {
        final List<String> columnsFromFile = List.of("column_str", "another_column");
        final String actualMessage = assertThrows(IllegalStateException.class, () ->
                ColumnNamesValidator.validate(columnsFromFile, DuplicateColumnNames.class)).getMessage();
        final String expectedMessage = "There are duplicates among the titles announced in @ExcelCell: " +
                "[column_str, column_str, another_column]";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateWithDuplicateColumnNamesInFileExceptionTest() {
        final List<String> columnsFromFile = List.of("column_str", "column_str", "another_column");
        final String actualMessage = assertThrows(IllegalStateException.class, () ->
                ColumnNamesValidator.validate(columnsFromFile, CorrectColumnNames.class)).getMessage();
        final String expectedMessage = "The columns in the file have a non-unique name. Columns: " +
                "[column_str, column_str, another_column]";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateWithoutRequiredFieldInFileExceptionTest() {
        final List<String> columnNamesFromFile = List.of("str_value", "offset_date_time_value");
        final String actualMessage = assertThrows(IllegalStateException.class, () ->
                ColumnNamesValidator.validate(columnNamesFromFile, CorrectColumnNames.class)).getMessage();
        final String expectedMessage = "The required \"int_value\" column is missing from the file";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateWithUnknownHeadersInFileExceptionTest() {
        final List<String> columnNamesFromFile = List.of("str_value", "int_value", "offset_date_time_value", "unknown_value");
        final String actualMessage = assertThrows(IllegalStateException.class, () ->
                ColumnNamesValidator.validate(columnNamesFromFile, CorrectColumnNames.class)).getMessage();
        final String expectedMessage = "The file contains unknown headers: [unknown_value]";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void validateWithoutDeclareColumnInPojoExceptionTest() {
        final List<String> columnNamesFromFile = List.of("val1", "val2");
        final String actualMessage = assertThrows(IllegalStateException.class, () ->
                ColumnNamesValidator.validate(columnNamesFromFile, WithoutColumns.class)).getMessage();
        final String expectedMessage = "There are no fields annotated by @ExcelCell in the class with the " +
                "ru.excel.converter.classes.WithoutColumns type.";
        assertEquals(expectedMessage, actualMessage);
    }
}
