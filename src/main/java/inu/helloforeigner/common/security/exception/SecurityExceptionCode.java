package inu.helloforeigner.common.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// common/security/exception/SecurityExceptionCode.java
@Getter
@RequiredArgsConstructor
public enum SecurityExceptionCode {
    INVALID_TOKEN("SEC_001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("SEC_002", "만료된 토큰입니다."),
    TOKEN_NOT_FOUND("SEC_003", "토큰이 존재하지 않습니다."),
    INVALID_SIGNATURE("SEC_004", "잘못된 토큰 서명입니다."),
    ACCESS_DENIED("SEC_005", "접근 권한이 없습니다."),
    ACCOUNT_LOCKED("SEC_006", "계정이 잠겼습니다. 관리자에게 문의하세요."),
    INVALID_CREDENTIALS("SEC_007", "잘못된 인증 정보입니다."),
    SESSION_EXPIRED("SEC_008", "세션이 만료되었습니다.");

    private final String code;
    private final String message;
}
