package wooteco.exception.badrequest;

public class NullColorException extends BadRequestException {

    public NullColorException() {
        super("색상 값이 입력되지 않았습니다.");
    }
}
