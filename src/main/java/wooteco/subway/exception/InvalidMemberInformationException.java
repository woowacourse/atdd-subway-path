package wooteco.subway.exception;

public class InvalidMemberInformationException extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 회원 정보 입니다.";

    public InvalidMemberInformationException() {
        super(MESSAGE);
    }
}
