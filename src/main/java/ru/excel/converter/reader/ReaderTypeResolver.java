package ru.excel.converter.reader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ReaderTypeResolver {

    /**
     * Возвращает нужный ExcelReader, подходящий для переданного поля
     *
     * @param field        поле класса, в которое нужно смапить значение
     * @param excelReaders классы для парсинга значения из ячейки
     * @return Соответствующий ExcelReader
     */
    public static @NotNull ExcelReader<?> resolve(@NotNull Field field,
                                                  @Nullable List<ExcelReader<?>> excelReaders) {
        final Class<?> fieldType = field.getType();
        if (CollectionUtils.isEmpty(excelReaders)) {
            throw new IllegalStateException("There is no ExcelReader implementation for the " + fieldType.getName() + " type");
        }
        //TODO Добавить логику по оверайдингу ExcelReader'a
        return excelReaders.get(0);
    }

}
