package wooteco.exception.badRequest;

public class NotExistedSectionException extends BadRequestException {
    public static final String MESSAGE = "등록하고자 하는 구간의 상행역 또는 하행역이 해당 라인에 등록되어 있어야 합니다.";

    public NotExistedSectionException() {
        super(MESSAGE);
    }
}
