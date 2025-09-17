package ru.excel.converter.reader.impl;

import org.junit.jupiter.params.provider.Arguments;
import ru.excel.converter.reader.NumberExcelReader;

import java.text.NumberFormat;
import java.util.stream.Stream;

public class DoubleReaderTest extends NumberExcelReaderBaseTest<Double> {
    @Override
    protected Class<? extends NumberExcelReader<Double>> getReaderType() {
        return DoubleReader.class;
    }

    @Override
    protected Stream<Arguments> getArgsForOkTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("1", 1.0))
                .add(Arguments.of("1.7976931348623157E308", Double.MAX_VALUE))
                .add(Arguments.of("4.9E-324", Double.MIN_VALUE))
                .add(Arguments.of("-43242423342.4", -43242423342.4))
                .add(Arguments.of("4535543.4334", 4535543.4334))
                .build();
    }

    @Override
    protected Stream<Arguments> getArgsForExceptionTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("128,5"))
                .add(Arguments.of("-129,2"))
                .add(Arguments.of("444.54.5"))
                .add(Arguments.of("incorrect"))
                .build();
    }
}
