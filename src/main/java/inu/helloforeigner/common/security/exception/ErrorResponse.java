package inu.helloforeigner.common.security.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

// common/security/exception/ErrorResponse.java
@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp;

    @Builder.Default
    private final String code = "ERROR";  // 에러 코드

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object data;  // 추가 데이터 필드
}
