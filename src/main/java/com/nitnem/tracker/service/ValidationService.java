package com.nitnem.tracker.service;

import com.nitnem.tracker.model.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidationService {

    public ValidationResult<Integer> validatePositiveInteger(
            String value,
            String fieldName,
            int min,
            int max
    ) {

        int parsedValue;

        try {

            parsedValue = Integer.parseInt(value.trim());

        } catch (NumberFormatException e) {

            return ValidationResult.<Integer>builder()
                    .valid(false)
                    .errorMessage(
                            fieldName + " must be a valid number"
                    )
                    .build();
        }

        if (parsedValue < min || parsedValue > max) {

            return ValidationResult.<Integer>builder()
                    .valid(false)
                    .errorMessage(
                            fieldName
                                    + " must be between "
                                    + min
                                    + " and "
                                    + max
                    )
                    .build();
        }

        return ValidationResult.<Integer>builder()
                .valid(true)
                .value(parsedValue)
                .build();
    }

    public ValidationResult<String> validateNitnemName(
            String nitnemName
    ) {

        if (nitnemName == null ||
                nitnemName.trim().isEmpty()) {

            return ValidationResult.<String>builder()
                    .valid(false)
                    .errorMessage(
                            "Nitnem name cannot be empty"
                    )
                    .build();
        }

        String trimmedName =
                nitnemName.trim();

        if (trimmedName.length() > 50) {

            return ValidationResult.<String>builder()
                    .valid(false)
                    .errorMessage(
                            "Nitnem name cannot exceed 50 characters"
                    )
                    .build();
        }

        if (!trimmedName.matches(
                "^[a-zA-Z0-9\\s]+$"
        )) {

            return ValidationResult.<String>builder()
                    .valid(false)
                    .errorMessage(
                            "Nitnem name can only contain letters, numbers and spaces"
                    )
                    .build();
        }

        return ValidationResult.<String>builder()
                .valid(true)
                .value(trimmedName)
                .build();
    }

    public ValidationResult<Integer> validateCurrentCount(
            int todayCount,
            int thresholdCount
    ) {

        int maxAllowed =
                thresholdCount * 20;

        if (todayCount > maxAllowed) {

            return ValidationResult.<Integer>builder()
                    .valid(false)
                    .errorMessage(
                            "Entered count looks unrealistic. Maximum allowed for this Nitnem is "
                                    + maxAllowed
                    )
                    .build();
        }

        return ValidationResult.<Integer>builder()
                .valid(true)
                .value(todayCount)
                .build();
    }
}
