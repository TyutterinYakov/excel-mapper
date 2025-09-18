package ru.excel.converter.reader.impl;

import org.junit.jupiter.params.provider.Arguments;
import ru.excel.converter.reader.NumberExcelReader;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class BigDecimalReaderTest extends NumberExcelReaderBaseTest<BigDecimal> {
    @Override
    protected Class<? extends NumberExcelReader<BigDecimal>> getReaderType() {
        return BigDecimalReader.class;
    }

    @Override
    protected Stream<Arguments> getArgsForOkTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("1", new BigDecimal("1")))
                .add(Arguments.of("0.34432321", new BigDecimal("0.34432321")))
                .add(Arguments.of("-34324432.34432321", new BigDecimal("-34324432.34432321")))
                .add(Arguments.of("3432443234443234332321", new BigDecimal("3432443234443234332321")))
                .build();
    }

    @Override
    protected Stream<Arguments> getArgsForExceptionTest() {
        return Stream.<Arguments>builder()
                .add(Arguments.of("dffd"))
                .add(Arguments.of("34234L"))
                .add(Arguments.of("incorrect"))
                .build();
    }
}
