package ru.excel.converter.reader.impl;

import org.dhatim.fastexcel.reader.Cell;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringReaderTest {

    private final StringReader stringReader = new StringReader(null);

    @ParameterizedTest
    @ValueSource(strings = {"test", " value ", "vvvv", "fdfFFfffgsR45453", "434321"})
    void covertTest(String expected) {
        final Cell cell = mock(Cell.class);
        when(cell.getRawValue()).thenReturn(expected);
        final String actual = stringReader.read(cell);
        assertEquals(expected.trim(), actual);
    }
}
