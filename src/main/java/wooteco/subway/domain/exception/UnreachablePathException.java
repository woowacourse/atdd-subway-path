package wooteco.subway.domain.exception;

import wooteco.subway.domain.Station;

public class UnreachablePathException extends IllegalArgumentException {

    public UnreachablePathException(Station source, Station target) {
        super(String.format("%s에서 %s까지 가는 경로가 없습니다.", source, target));
    }
}
