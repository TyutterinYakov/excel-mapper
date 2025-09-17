package ru.excel.converter.reader.impl;

import org.junit.jupiter.params.provider.Arguments;
import ru.excel.converter.reader.NumberExcelReader;

import java.util.stream.Stream;

public class ByteReaderTest extends NumberExcelReaderBaseTest<Byte> {

    @Override
    protected Class<? extends NumberExcelReader<Byte>> getReaderType() {
        return ByteReader.class;
    }

    @Override
    protected Stream<Arguments> getArgsForOkTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("1", (byte) 1))
                .add(Arguments.of("0", (byte) 0))
                .add(Arguments.of("-128", (byte) -128))
                .add(Arguments.of("127", (byte) 127))
                .build();
    }

    @Override
    protected Stream<Arguments> getArgsForExceptionTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("128"))
                .add(Arguments.of("-129"))
                .add(Arguments.of("444"))
                .add(Arguments.of("333"))
                .add(Arguments.of("incorrect"))
                .build();
    }
}
