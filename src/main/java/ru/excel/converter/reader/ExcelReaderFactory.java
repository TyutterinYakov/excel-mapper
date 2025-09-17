package ru.excel.converter.reader;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.excel.converter.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ExcelReaderFactory {
    private final Map<Class<?>, Map<Class<ExcelReader<?>>, ExcelReader<?>>> readersByType;

    @SuppressWarnings("unchecked")
    public ExcelReaderFactory(List<ExcelReader<?>> readers) {
        //TODO Добавить логику по добавлению глобального оверайдера базового типа
        //Извлекаем типы из дженериков и на его основе формируем мапу readersByType
        readersByType = new HashMap<>();
        readers.stream().forEach(r -> {
            final Class<?> type = ReflectionUtil.getTypeFromGeneric(r.getClass());
            readersByType.computeIfAbsent(type, k -> new HashMap<>()).put((Class<ExcelReader<?>>) r.getClass(), r);
        });
    }

    public @NotNull ExcelReader<?> getReader(@NotNull Field field) {
        Objects.requireNonNull(field);
        return ReaderTypeResolver.resolve(field, readersByType.get(field.getType()));
    }
}
