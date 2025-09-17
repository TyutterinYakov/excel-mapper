package ru.excel.converter.reader.impl;

import org.junit.jupiter.params.provider.Arguments;
import ru.excel.converter.reader.NumberExcelReader;

import java.util.stream.Stream;

public class FloatReaderTest extends NumberExcelReaderBaseTest<Float> {
    @Override
    protected Class<? extends NumberExcelReader<Float>> getReaderType() {
        return FloatReader.class;
    }

    @Override
    protected Stream<Arguments> getArgsForOkTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("1.0", 1.0f))
                .add(Arguments.of("3.4028235E38", Float.MAX_VALUE))
                .add(Arguments.of("1.4E-45", Float.MIN_VALUE))
                .add(Arguments.of("-43242423342.4", -43242423342.4f))
                .add(Arguments.of("4535543.4334", 4535543.4334f))
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
