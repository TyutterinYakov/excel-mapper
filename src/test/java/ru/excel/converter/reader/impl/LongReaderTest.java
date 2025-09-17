package ru.excel.converter.reader.impl;

import org.junit.jupiter.params.provider.Arguments;
import ru.excel.converter.reader.NumberExcelReader;

import java.util.stream.Stream;

public class LongReaderTest extends NumberExcelReaderBaseTest<Long> {
    @Override
    protected Class<? extends NumberExcelReader<Long>> getReaderType() {
        return LongReader.class;
    }

    @Override
    protected Stream<Arguments> getArgsForOkTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("1", 1L))
                .add(Arguments.of("-34223432234432", -34223432234432L))
                .add(Arguments.of("9223372036854775807", Long.MAX_VALUE))
                .add(Arguments.of("-9223372036854775808", Long.MIN_VALUE))
                .build();
    }

    @Override
    protected Stream<Arguments> getArgsForExceptionTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("999999999999999999999999999999999999999999999999"))
                .add(Arguments.of("-99999999999999999999999999999999999999999999999999999999"))
                .add(Arguments.of("444.3"))
                .add(Arguments.of("333,4"))
                .add(Arguments.of("incorrect"))
                .build();
    }
}
