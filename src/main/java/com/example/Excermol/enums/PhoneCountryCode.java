package com.example.Excermol.enums;

public enum PhoneCountryCode {
    AZERBAIJAN("+994"), TURKIYE("+90"), RUSSIA("+7"), GEORGIA("+995"), IRAN("+98");

    private final String code;

    PhoneCountryCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
