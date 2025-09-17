package ru.excel.converter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.excel.converter.classes.OnlyDateTypes;
import ru.excel.converter.classes.ThreeTypesForMapping;
import ru.excel.converter.classes.WithAllDefaultJavaTypes;
import ru.excel.converter.classes.WithCustomReader;
import ru.excel.converter.exception.ExcelParsingException;

import java.time.*;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.excel.converter.util.TestUtil.readFile;

@SpringBootTest
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

        final List<OnlyDateTypes> expected = List.of(
                OnlyDateTypes.builder()
                        .offsetDateTimeValue(OffsetDateTime.parse("2025-09-15T15:40:37.180187+03:00"))
                        .instantValue(Instant.parse("2025-09-15T12:40:22.020026Z"))
                        .localDateTimeValue(LocalDateTime.parse("2025-09-15T15:39:32.559607"))
                        .localDateValue(LocalDate.of(2025, 12, 10))
                        .customLocalDateValueFormat(LocalDate.of(2025, 12, 10))
                        .customLocalDateTimeValueFormat(LocalDateTime.of(2022, 1, 14, 13, 0, 12))
                        .customOffsetDateTimeValueFormat(OffsetDateTime.of(2025, 9, 15, 15, 40, 37, 180187000, ZoneOffset.ofHours(2)))
                        .build(),
                OnlyDateTypes.builder()
                        .offsetDateTimeValue(OffsetDateTime.parse("2025-09-15T15:40:37.180187+03:00"))
                        .instantValue(Instant.parse("2025-09-15T12:40:22.020026Z"))
                        .localDateTimeValue(LocalDateTime.parse("2025-01-11T15:33:32.54"))
                        .localDateValue(LocalDate.of(2025, 1, 14))
                        .customLocalDateValueFormat(LocalDate.of(2025, 12, 11))
                        .customLocalDateTimeValueFormat(LocalDateTime.of(2020, 1, 15, 13, 0, 12))
                        .customOffsetDateTimeValueFormat(OffsetDateTime.of(2025, 9, 15, 15, 40, 37, 180187000, ZoneOffset.ofHours(5)))
                        .build(),
                OnlyDateTypes.builder()
                        .offsetDateTimeValue(OffsetDateTime.parse("2025-09-15T15:40:37.180187+03:00"))
                        .instantValue(Instant.parse("2025-09-15T12:40:22.020026Z"))
                        .localDateTimeValue(LocalDateTime.parse("2024-03-11T15:39:32.559607"))
                        .localDateValue(LocalDate.of(2025, 2, 11))
                        .customLocalDateValueFormat(LocalDate.of(2025, 12, 11))
                        .build(),
                OnlyDateTypes.builder()
                        .localDateValue(LocalDate.of(2024, 9, 10))
                        .customOffsetDateTimeValueFormat(OffsetDateTime.of(2025, 9, 15, 15, 40, 37, 180187000, ZoneOffset.UTC))
                        .build()
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void customReaderTest() {
        final List<WithCustomReader> actual = excelParser
                .parse(readFile("/files/custom_reader.xlsx"), WithCustomReader.class, 0);
        assertEquals(3, actual.size());
        final String testValue = "THIS IS TEST CUSTOM READER VALUE";

        final List<WithCustomReader> expected = List.of(
                WithCustomReader.builder().strValue("str1").customExcelReaderValue(testValue).build(),
                WithCustomReader.builder().strValue("str2").customExcelReaderValue(testValue).build(),
                WithCustomReader.builder().strValue("str3").customExcelReaderValue(testValue).build());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void exceptionTest() {
        final Map<Integer, Map<Integer, Map<String, List<String>>>> actualErrors = assertThrows(ExcelParsingException.class, () ->
                excelParser.parse(readFile("/files/exception_test.xlsx"), ThreeTypesForMapping.class, 0)).getErrors();

        Map<Integer, Map<String, List<String>>> firstRow = Map.of(
                0, Map.of("long_value", List.of("The value \"lala\" does not match the type \"java.lang.Long\"")),
                2, Map.of("bool_value", List.of("Incorrect value \"342.0\" for type \"java.lang.Boolean\"")));

        Map<Integer, Map<String, List<String>>> secondRow = Map.of(
                2, Map.of("bool_value", List.of("Incorrect value \"rtrtr\" for type \"java.lang.Boolean\"")));

        Map<Integer, Map<String, List<String>>> thirdRow = Map.of(
                0, Map.of("long_value", List.of("The value \"trttr\" does not match the type \"java.lang.Long\"")));

        final Map<Integer, Map<Integer, Map<String, List<String>>>> expectedErrors = Map.of(
                1, firstRow,
                2, secondRow,
                3, thirdRow
        );

        assertThat(actualErrors).isEqualTo(expectedErrors);


    }
}
