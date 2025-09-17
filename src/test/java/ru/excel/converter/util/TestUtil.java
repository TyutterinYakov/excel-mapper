package ru.excel.converter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtil {

    public static File readFile(String path) {
        return new File(Objects.requireNonNull(TestUtil.class.getResource(path)).getPath());
    }
}
