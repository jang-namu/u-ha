package inu.helloforeigner.common.exception;

public class InterestNotFoundException extends RuntimeException {
    public InterestNotFoundException() {
        super("해당 관심사를 찾을 수 없습니다.");
    }

    public InterestNotFoundException(String interest) {
        super(interest + "에 해당하는 관심사를 찾을 수 없습니다.");
    }
}
