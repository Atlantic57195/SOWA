package com.berkdagli.sowa.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        if (password.length() < 8) {
            context.buildConstraintViolationWithTemplate("Password must be at least 8 characters long")
                    .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(".*[A-Z].*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter")
                    .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(".*[a-z].*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one lowercase letter")
                    .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(".*[0-9].*")) {
            context.buildConstraintViolationWithTemplate("Password must contain at least one digit")
                    .addConstraintViolation();
            isValid = false;
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one special character (@#$%^&+=!)")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
