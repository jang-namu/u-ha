package inu.helloforeigner.common.exception;

public class ChatRoomNotFoundException extends RuntimeException {

    public ChatRoomNotFoundException(Long chatRoomId) {
        super(chatRoomId + "번 채팅방을 찾을 수 없습니다.");
    }
}
