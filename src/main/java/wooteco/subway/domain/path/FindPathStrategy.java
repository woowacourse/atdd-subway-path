package wooteco.subway.domain.path;

import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.NotFoundException;

public interface FindPathStrategy {

    Path findPath(Station source, Station target, Sections sections);

    default NotFoundException notFoundPathException() {
        return new NotFoundException("갈 수 있는 경로를 찾을 수 없습니다.");
    }
}
