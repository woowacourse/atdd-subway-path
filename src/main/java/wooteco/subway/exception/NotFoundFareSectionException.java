package wooteco.subway.exception;

public class NotFoundFareSectionException extends SubwayNotFoundException {

    private static final String NOT_FOUND_MESSAGE = "요금 계산을 위한 구간 확인에 실패했습니다 : ";

    public NotFoundFareSectionException(long distance) {
        super(NOT_FOUND_MESSAGE + distance);
    }
}
