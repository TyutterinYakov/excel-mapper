package ru.excel.converter.reader;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.excel.converter.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ExcelReaderFactory {
    private final Map<Class<?>, Map<Class<ExcelReader<?>>, ExcelReader<?>>> readersByType;

    @SuppressWarnings("unchecked")
    public ExcelReaderFactory(List<ExcelReader<?>> readers) {
        readersByType = new HashMap<>();
        readers.forEach(r -> {
            final Class<?> type = ReflectionUtil.getTypeFromGeneric(r.getClass());
            readersByType.computeIfAbsent(type, k -> new HashMap<>()).put((Class<ExcelReader<?>>) r.getClass(), r);
        });
    }

    public @NotNull ExcelReader<?> getReader(@NotNull Field field) {
        Objects.requireNonNull(field);
        return ReaderTypeResolver.resolve(field, readersByType.get(field.getType()));
    }
}
