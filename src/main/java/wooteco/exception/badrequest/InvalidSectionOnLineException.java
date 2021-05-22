package wooteco.exception.badrequest;

public class InvalidSectionOnLineException extends BadRequestException {

    public InvalidSectionOnLineException() {
        super("해당 노선에 입력된 구간을 추가할 수 없습니다.");
    }
}
