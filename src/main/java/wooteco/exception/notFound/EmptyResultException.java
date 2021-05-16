package wooteco.exception.notFound;

public class EmptyResultException extends NotFoundException {
    public static final String MESSAGE = "요청에 해당하는 결과값이 존재하지 않습니다. ";

    public EmptyResultException() {
        super(MESSAGE);
    }
}
