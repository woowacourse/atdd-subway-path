package wooteco.exception.badRequest;

public class AlreadyExistedSectionException extends BadRequestException {
    public static final String MESSAGE = "이미 존재하는 구간이어서 추가할 수 없습니다.";

    public AlreadyExistedSectionException() {
        super(MESSAGE);
    }
}
