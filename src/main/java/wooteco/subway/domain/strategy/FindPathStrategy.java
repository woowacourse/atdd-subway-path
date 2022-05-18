package wooteco.subway.domain.strategy;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.NotFoundException;

public interface FindPathStrategy {

    Path findPath(Station source, Station target, Sections sections);

    default NotFoundException notFoundPathException() {
        return new NotFoundException("갈 수 있는 경로를 찾을 수 없습니다.");
    }
}
