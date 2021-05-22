package wooteco.exception.badrequest;

public class NullIdException extends BadRequestException {

    public NullIdException() {
        super("아이디 값이 입력되지 않았습니다.");
    }
}
