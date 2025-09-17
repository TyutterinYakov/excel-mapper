package ru.excel.converter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtil {

    public static Class<?> getTypeFromGeneric(Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public static @NotNull List<Field> getDeclaredFieldsWithAnnotation(@NotNull Class<?> type,
                                                                       @NotNull Class<? extends Annotation> annotation) {
        final List<Field> fields = new ArrayList<>();
        while (type != Object.class) {
            fields.addAll(Arrays.stream(type.getDeclaredFields()).filter(f ->
                    f.isAnnotationPresent(annotation)).toList());
            type = type.getSuperclass();
        }
        return fields;
    }

    @SneakyThrows
    public static <T> void setValueToField(Field field, Object value, T obj) {
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static  <T> @NotNull T newInstance(@NotNull Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException("No such default constructor without argument in: " + type.getName());
        }
    }

}
