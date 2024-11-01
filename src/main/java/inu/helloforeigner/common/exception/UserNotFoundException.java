package inu.helloforeigner.common.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("해당 유저를 찾을 수 없습니다.");
    }

    public UserNotFoundException(Long id) {
        super(id + "번 유저를 찾을 수 없습니다.");
    }
}
