package wooteco.subway.admin.exception;

public class DuplicateStationException extends BusinessException {
    public DuplicateStationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
