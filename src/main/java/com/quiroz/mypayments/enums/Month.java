package com.quiroz.mypayments.enums;

import java.util.stream.Stream;

public enum Month {
    ENERO("Enero", "January"),
    FEBRERO("Febrero", "February"),
    MARZO("Marzo", "March"),
    ABRIL("Abril", "April"),
    MAYO("Mayo", "May"),
    JUNIO("Junio", "June"),
    JULIO("Julio", "July"),
    AGOSTO("Agosto", "August"),
    SEPTIEMBRE("Septiembre", "September"),
    OCTUBRE("Octubre", "October"),
    NOVIEMBRE("Noviembre", "November"),
    DICIEMBRE("Diciembre", "December");


    private String spanishName;
    private String englishName;

    Month(String spanishName, String englishName) {
        this.spanishName = spanishName;
        this.englishName = englishName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getSpanishName() {
        return spanishName;
    }

    public static Month getByNameSpanish(String nameSpanish) {
        return Stream.of(Month.values())
                .filter(m -> m.getSpanishName().equals(nameSpanish))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Month getByNameEnglish(String nameEnglish) {
        return Stream.of(Month.values())
                .filter(m -> m.getEnglishName().equals(nameEnglish))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
