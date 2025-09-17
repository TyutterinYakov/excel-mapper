package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.support.StaticMessageSource;
import ru.excel.converter.exception.CellExcelReaderException;

import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BooleanReaderTest {
    private BooleanReader booleanReader;

    @BeforeEach
    void init() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("reader.boolean.incorrectValue", Locale.getDefault(),
                "Incorrect value \"{0}\" for type Boolean");
        booleanReader  = new BooleanReader(messageSource);
    }

    @ParameterizedTest
    @MethodSource("getArgsForReadTest")
    void readTest(String value, Boolean expected) {
        final Cell cell = mock(Cell.class);
        when(cell.getRawValue()).thenReturn(value);
        final Boolean actual = booleanReader.read(cell);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2", "tRu", "fAls", "3", "CORRECT", ""})
    void readExceptionTest(String value) {
        final Cell cell = mock(Cell.class);
        when(cell.getRawValue()).thenReturn(value);
        final String actual = assertThrows(CellExcelReaderException.class, () ->
                booleanReader.read(cell)).getMessage();
        final String expected = "Incorrect value \"%s\" for type Boolean".formatted(value);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> getArgsForReadTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("1", true))
                .add(Arguments.of("0", false))
                .add(Arguments.of("false", false))
                .add(Arguments.of("true", true))
                .add(Arguments.of("TRUE", true))
                .add(Arguments.of("FALSE", false))
                .add(Arguments.of("fAlse", false))
                .add(Arguments.of("TruE", true))
                .build();
    }
}
