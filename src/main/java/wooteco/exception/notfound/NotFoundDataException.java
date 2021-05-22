package wooteco.exception.notfound;

public class NotFoundDataException extends NotFoundException {

    public NotFoundDataException() {
        super("해당 정보가 존재하지 않습니다.");
    }
}
