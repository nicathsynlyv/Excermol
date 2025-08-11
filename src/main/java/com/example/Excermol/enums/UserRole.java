package com.example.Excermol.enums;

public enum UserRole {
    ADMIN("Administrator"),
    MANAGER("Menecer"),
    USER("İstifadəçi"),
    VIEWER("Baxışçı");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canManage() {
        return this == ADMIN || this == MANAGER;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

}
