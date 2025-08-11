package com.example.Excermol.enums;

public enum UserStatus {
    ACTIVE("Aktiv"),
    INACTIVE("Qeyri-aktiv"),
    SUSPENDED("Dayandırılmış");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }



}



