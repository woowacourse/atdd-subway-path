package wooteco.subway.admin.exception;

public class StationNotFoundException extends BusinessException {
    public StationNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
