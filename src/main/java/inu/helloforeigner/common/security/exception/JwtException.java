package inu.helloforeigner.common.security.exception;

import inu.helloforeigner.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final SecurityExceptionCode errorCode;

    public JwtException(SecurityExceptionCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JwtException(SecurityExceptionCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
