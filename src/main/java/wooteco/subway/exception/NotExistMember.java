package wooteco.subway.exception;

public class NotExistMember extends RuntimeException {

    private static final String MESSAGE = "등록된 회원이 아닙니다.";

    public NotExistMember() {
        super(MESSAGE);
    }

}
