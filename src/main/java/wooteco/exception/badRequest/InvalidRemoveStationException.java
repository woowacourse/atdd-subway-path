package wooteco.exception.badRequest;

public class InvalidRemoveStationException extends BadRequestException {
    public static final String MESSAGE = "구간이 1개일 때에는 역을 삭제할 수 없습니다.";

    public InvalidRemoveStationException() {
        super(MESSAGE);
    }
}
