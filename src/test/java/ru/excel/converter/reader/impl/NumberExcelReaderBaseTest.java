package ru.excel.converter.reader.impl;

import lombok.SneakyThrows;
import org.dhatim.fastexcel.reader.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import ru.excel.converter.exception.CellExcelReaderException;
import ru.excel.converter.reader.NumberExcelReader;
import ru.excel.converter.reader.customization.ReaderCustomization;
import ru.excel.converter.util.ReflectionUtil;

import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class NumberExcelReaderBaseTest<T extends Number> {

    private NumberExcelReader<T> excelReader;

    protected abstract Class<? extends NumberExcelReader<T>> getReaderType();
    protected abstract Stream<Arguments> getArgsForOkTest();
    protected abstract Stream<Arguments> getArgsForExceptionTest();

    @BeforeEach
    void init() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("reader.number.incorrectValue", Locale.getDefault(),
                "The value \"{0}\" does not match the type \"{1}\"");
        excelReader = getReader(messageSource);
    }

    @ParameterizedTest
    @MethodSource("getArgsForOkTest")
    void convertOkTest(String strValue, T expected) {
        final Cell cell = mock(Cell.class);
        when(cell.getRawValue()).thenReturn(strValue);
        when(cell.getText()).thenReturn(strValue);
        final T actual = excelReader.read(cell, new ReaderCustomization());
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getArgsForExceptionTest")
    void convertExceptionTest(String strValue) {
        final Cell cell = mock(Cell.class);
        when(cell.getRawValue()).thenReturn(strValue);
        when(cell.getText()).thenReturn(strValue);
        final String actual = assertThrows(CellExcelReaderException.class, () ->
                excelReader.read(cell, new ReaderCustomization())).getMessage();
        final String expected = "The value \"%s\" does not match the type \"%s\"".formatted(strValue, getType().getName());
        assertEquals(expected, actual);
    }

    @SneakyThrows
    private NumberExcelReader<T> getReader(MessageSource messageSource) {
        return getReaderType().getConstructor(MessageSource.class).newInstance(messageSource);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getType() {
        return (Class<T>) ReflectionUtil.getTypeFromGeneric(getClass());
    }
}
