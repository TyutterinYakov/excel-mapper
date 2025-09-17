package ru.excel.converter.reader;

import lombok.RequiredArgsConstructor;
import org.dhatim.fastexcel.reader.Cell;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import ru.excel.converter.exception.CellExcelReaderException;
import ru.excel.converter.reader.customization.ReaderCustomization;

@RequiredArgsConstructor
public abstract class ExcelReader<T> {
    protected final MessageSource messageSource;
    /**
     * Парсит полученное значение в виде строки в Java-тип, соответствующий <T>
     * @param cell значение из ячейки excel-файла
     * @return Значение с типом <T>
     * @throws CellExcelReaderException Если полученное значения невозможно спарсить в тип <T>
     */
    //TODO: Можно было бы добавить постОбработчик для значения, которое мы получаем из cell. Но нужно подумать,
    // так как не всегда значение передается именно в виде текста. А модифицировать Cell нельзя
    public abstract @NotNull T read(@NotNull Cell cell, @NotNull ReaderCustomization readerCustomization);
}
