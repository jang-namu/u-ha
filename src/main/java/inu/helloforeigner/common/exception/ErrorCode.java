package inu.helloforeigner.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// common/exception/ErrorCode.java
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    RESOURCE_NOT_FOUND(404, "C002", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류가 발생했습니다."),

    // User
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(409, "U002", "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(400, "U003", "비밀번호가 올바르지 않습니다."),

    // Chat
    CHAT_ROOM_NOT_FOUND(404, "CH001", "채팅방을 찾을 수 없습니다."),
    CHAT_ACCESS_DENIED(403, "CH002", "채팅방에 접근할 수 없습니다."),

    // Feed
    FEED_NOT_FOUND(404, "F001", "피드를 찾을 수 없습니다."),
    FEED_ACCESS_DENIED(403, "F002", "피드에 접근할 수 없습니다."),

    // File
    FILE_UPLOAD_ERROR(500, "FI001", "파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE(400, "FI002", "지원하지 않는 파일 형식입니다.");

    private final int status;
    private final String code;
    private final String message;
}
