package ru.excel.converter.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class ExcelConverterConfiguration {
    @Bean
    @Primary
    public MessageSource excelConverterMessageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:excel_converter_messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
