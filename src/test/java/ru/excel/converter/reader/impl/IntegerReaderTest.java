package ru.excel.converter.reader.impl;

import org.junit.jupiter.params.provider.Arguments;
import ru.excel.converter.reader.NumberExcelReader;

import java.util.stream.Stream;

public class IntegerReaderTest extends NumberExcelReaderBaseTest<Integer> {
    @Override
    protected Class<? extends NumberExcelReader<Integer>> getReaderType() {
        return IntegerReader.class;
    }

    @Override
    protected Stream<Arguments> getArgsForOkTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("1", 1))
                .add(Arguments.of("0", 0))
                .add(Arguments.of("-128", -128))
                .add(Arguments.of("127", 127))
                .add(Arguments.of("2147483647", Integer.MAX_VALUE))
                .add(Arguments.of("-2147483648", Integer.MIN_VALUE))
                .build();
    }

    @Override
    protected Stream<Arguments> getArgsForExceptionTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("2147483648"))
                .add(Arguments.of("-2147483649"))
                .add(Arguments.of("444.3"))
                .add(Arguments.of("333,4"))
                .add(Arguments.of("incorrect"))
                .build();
    }
}
