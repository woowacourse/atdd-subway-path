package wooteco.subway.exception;

public class PathNotLinkedException extends SubwayBadRequestException {

    private static final String MESSAGE = "연결되지 않은 구간입니다.";

    public PathNotLinkedException() {
        super(MESSAGE);
    }
}
