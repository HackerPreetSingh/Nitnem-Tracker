package com.nitnem.tracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidationResult<T> {

    private boolean valid;

    private T value;

    private String errorMessage;
}
