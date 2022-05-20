package wooteco.subway.exception.notfound;

import wooteco.subway.exception.SubwayNotFoundException;

public class NotFoundPathException extends SubwayNotFoundException {

    private static final String DEFAULT_MESSAGE = "[%s -> %s] 출발지부터 도착지까지 구간이 연결되어 있지 않습니다";

    public NotFoundPathException(Long sourceId, Long targetId) {
        super(String.format(DEFAULT_MESSAGE, sourceId, targetId));
    }
}
