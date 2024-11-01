package inu.helloforeigner.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

// common/exception/ErrorResponse.java
@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<ValidationError> errors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String path;
}
