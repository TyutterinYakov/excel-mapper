package ru.excel.converter.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ColumnNameFormatterTest {

    @Test
    void formattingTest() {
        final String actual = ColumnNameFormatter.formatting(" TeSt_coLumn ");
        assertEquals("test_column", actual);
    }

    @ParameterizedTest
    @MethodSource("getArgsForExceptionTest")
    void formattingExceptionTest(String val) {
        final String actualMessage = assertThrows(IllegalStateException.class, () ->
                ColumnNameFormatter.formatting(val)).getMessage();
        assertEquals("The column name cannot be empty or consist only of spaces", actualMessage);
    }

    private static Stream<Arguments> getArgsForExceptionTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of((String) null))
                .add(Arguments.of(""))
                .add(Arguments.of("   "))
                .build();
    }
}
