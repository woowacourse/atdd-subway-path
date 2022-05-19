package wooteco.subway.exception;

import wooteco.subway.domain.Station;

public class PathNotFoundException extends BusinessException{

    public PathNotFoundException(String message) {
        super(message);
    }

    public PathNotFoundException(Station source, Station target) {
        super(String.format("출발역 %s과 도착역 %s 중 구간에 존재하지 않는 역이 있습니다", source.getName(), target.getName()));
    }
}
