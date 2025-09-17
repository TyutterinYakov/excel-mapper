package ru.excel.converter.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CellParsingException extends RuntimeException {
    private final Map<Integer, Map<Integer, Map<String, List<String>>>> errors;

    public CellParsingException() {
        this(new HashMap<>());
    }

    public CellParsingException(Map<Integer, Map<Integer, Map<String, List<String>>>> errors) {
        this.errors = errors;
    }

    public void addError(int rowNum, int colNum, @NotNull String colName, @NotNull String error) {
        this.addError(rowNum, colNum, colName, List.of(error));
    }

    public void addError(int rowNum, int colNum, @NotNull String colName, @NotNull List<String> errorList) {
        final Map<Integer, Map<String, List<String>>> rowErrors = errors.computeIfAbsent(rowNum, v -> new HashMap<>());
        final Map<String, List<String>> columnError = rowErrors.computeIfAbsent(colNum, v -> new HashMap<>());
        final List<String> cellError = columnError.computeIfAbsent(colName, v -> new ArrayList<>());
        cellError.addAll(errorList);
    }

}
