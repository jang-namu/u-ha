package inu.helloforeigner.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// common/exception/ValidationError.java
@Getter
@AllArgsConstructor
public class ValidationError {
    private final String field;
    private final String value;
    private final String reason;
}
