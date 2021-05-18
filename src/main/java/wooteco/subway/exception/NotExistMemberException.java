package wooteco.subway.exception;

public class NotExistMemberException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 회원 입니다.";

    public NotExistMemberException() {
        super(MESSAGE);
    }

}
