package com.quiroz.mypayments.enums;

public enum Categories {
    SERVICIOS_BASICOS("Servicios Basicos"),
    VIVIENDA("Vivienda"),
    DEUDAS("Deudas"),
    AUTOMOVIL("Automovil"),
    SEGUROS("Seguros"),
    EDUCACION("Educacion"),
    TRANSPORTE("Transporte"),
    SALARIOS("Salarios"),
    VIAJES_Y_PASEOS("Viajes y Paseos"),
    GASTOS_PERSONALES_ARIEL("Gastos Personales Ariel"),
    GASTOS_PERSONALES_ALINA("Gastos Personales Alina"),
    GASTOS_PERSONALES_GAEL("Gastos Personales Gael"),
    ALIMENTOS("Alimentos"),
    SALUD("Salud"),
    ALIMENTACION_GUSTOS("Alimentacion Gustos"),
    ARTICULOS_LIMPIEZA_CASA("Articulos de Limpieza - Casa"),
    CUMPLES_Y_DIAS_ESPECIALES("Cumples y dias especiales"),
    MASCOTAS("Mascotas"),
    OTROS_GASTOS("Otros Gastos");

    private String name;

    Categories(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
