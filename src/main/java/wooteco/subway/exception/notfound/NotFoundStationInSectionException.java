package wooteco.subway.exception.notfound;

import wooteco.subway.exception.SubwayNotFoundException;

public class NotFoundStationInSectionException extends SubwayNotFoundException {

    private static final String NOT_FOUND_MESSAGE = "요청한 지하철역이 연결된 구간 존재하지 않습니다";

    public NotFoundStationInSectionException() {
        super(NOT_FOUND_MESSAGE);
    }

    public NotFoundStationInSectionException(Long id) {
        super(NOT_FOUND_MESSAGE + " : " + id);
    }
}
