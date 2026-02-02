package com.example.ihatemylife;

/**
 * Centralized validation helpers for contact creation.
 * Easily extensible for additional validation rules.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006\u00a8\u0006\n"}, d2 = {"Lcom/example/ihatemylife/ContactValidator;", "", "()V", "validateContactMethod", "Lcom/example/ihatemylife/ValidationResult;", "email", "", "phone", "validateEmail", "validatePhone", "app_debug"})
public final class ContactValidator {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ihatemylife.ContactValidator INSTANCE = null;
    
    private ContactValidator() {
        super();
    }
    
    /**
     * Validates phone number: must be exactly 11 digits (excluding +7 prefix).
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.ihatemylife.ValidationResult validatePhone(@org.jetbrains.annotations.NotNull()
    java.lang.String phone) {
        return null;
    }
    
    /**
     * Validates email format and domain.
     * Must not contain spaces, have valid format, and belong to common email domains.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.ihatemylife.ValidationResult validateEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
        return null;
    }
    
    /**
     * Validates that at least one contact method (email or phone) is provided and valid.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.ihatemylife.ValidationResult validateContactMethod(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String phone) {
        return null;
    }
}