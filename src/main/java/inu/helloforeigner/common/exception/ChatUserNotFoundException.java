package inu.helloforeigner.common.exception;

public class ChatUserNotFoundException extends RuntimeException {

    public ChatUserNotFoundException(Long chatUserId) {
        super("채팅 유저를 찾을 수 없습니다. chatUserId: " + chatUserId);
    }
}