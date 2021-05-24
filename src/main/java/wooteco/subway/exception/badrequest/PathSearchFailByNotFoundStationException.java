package wooteco.subway.exception.badrequest;

public class PathSearchFailByNotFoundStationException extends BadRequestException{
    private static final String MESSAGE = "요청한 역이 존재하지않아 경로 탐색에 실패했습니다.";

    public PathSearchFailByNotFoundStationException() {
        super(MESSAGE);
    }

    public PathSearchFailByNotFoundStationException(Throwable cause) {
        super(MESSAGE, cause);
    }
}