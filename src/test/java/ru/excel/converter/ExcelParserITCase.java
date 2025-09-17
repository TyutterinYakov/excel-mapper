package ru.excel.converter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.excel.converter.classes.OnlyDateTypes;
import ru.excel.converter.classes.ThreeTypesForMapping;
import ru.excel.converter.classes.WithAllDefaultJavaTypes;
import ru.excel.converter.configuration.ExcelConverterConfiguration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.excel.converter.util.TestUtil.readFile;

@SpringBootTest
@Import(ExcelConverterConfiguration.class)
public class ExcelParserITCase {
    @Autowired
    private ExcelParser excelParser;

    @Test
    void test() {
        final List<ThreeTypesForMapping> actual = excelParser
                .parse(readFile("/files/ThreeColumnCorrect.xlsx"), ThreeTypesForMapping.class, 0);
        assertEquals(3, actual.size());

        final List<ThreeTypesForMapping> expected = List.of(
                ThreeTypesForMapping.builder().longValue(11111111111L).strValue("String1").boolValue(true).build(),
                ThreeTypesForMapping.builder().longValue(222222222222L).strValue("String2").boolValue(false).build(),
                ThreeTypesForMapping.builder().longValue(3333333333333L).strValue("String3").boolValue(true).build()
                );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void withAllDefaultJavaTypesTest() {
        final List<WithAllDefaultJavaTypes> actual = excelParser
                .parse(readFile("/files/with_all_java_types.xlsx"), WithAllDefaultJavaTypes.class, 0);
        assertEquals(3, actual.size());

        final List<WithAllDefaultJavaTypes> expected = List.of(
                WithAllDefaultJavaTypes.builder().strValue("String1").longValue(11111111111L).boolValue(true)
                        .offsetDateTimeValue(OffsetDateTime.parse("2025-09-15T15:40:37.180187+03:00"))
                        .instantValue(null)
                        .localDateTimeValue(LocalDateTime.parse("2025-09-15T15:39:32.559607"))
                        .localDateValue(LocalDate.parse("2025-09-15"))
                        .floatValue(545.43f)
                        .intValue(4334)
                        .byteValue((byte) 1)
                        .build(),
                WithAllDefaultJavaTypes.builder().strValue("String2").longValue(222222222222L).boolValue(false)
                        .offsetDateTimeValue(null)
                        .instantValue(Instant.parse("2025-09-15T12:40:22.020026Z"))
                        .localDateTimeValue(LocalDateTime.parse("2025-01-11T15:33:32.54"))
                        .doubleValue(654.6773)
                        .floatValue(775.1f)
                        .intValue(6573)
                        .build(),
                WithAllDefaultJavaTypes.builder().strValue("String3").longValue(3333333333333L).boolValue(true)
                        .offsetDateTimeValue(OffsetDateTime.parse("2025-09-15T15:40:37.180187+03:00"))
                        .instantValue(Instant.parse("2025-09-15T12:40:22.020026Z"))
                        .localDateTimeValue(null)
                        .localDateValue(LocalDate.parse("2025-02-11"))
                        .doubleValue(54.4)
                        .intValue(-3445)
                        .byteValue((byte) 127)
                        .build()
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void onlyDateTest() {
        final List<OnlyDateTypes> actual = excelParser
                .parse(readFile("/files/only_date.xlsx"), OnlyDateTypes.class, 0);
        assertEquals(4, actual.size());
    }
}
