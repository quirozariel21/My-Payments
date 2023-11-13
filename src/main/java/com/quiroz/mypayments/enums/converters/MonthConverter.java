package com.quiroz.mypayments.enums.converters;

import com.quiroz.mypayments.enums.Month;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class MonthConverter implements AttributeConverter<Month, String> {
    @Override
    public String convertToDatabaseColumn(Month month) {
        if (month == null)
            return null;
        return month.getSpanishName();
    }

    @Override
    public Month convertToEntityAttribute(String codeSpanish) {
        if (codeSpanish == null)
            return null;

        return Stream.of(Month.values())
                .filter(m -> m.getSpanishName().equals(codeSpanish))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
