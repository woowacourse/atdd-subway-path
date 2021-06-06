package wooteco.exception.badrequest;

public class NullNameException extends BadRequestException {

    public NullNameException() {
        super("이름 값이 입력되지 않았습니다.");
    }
}
