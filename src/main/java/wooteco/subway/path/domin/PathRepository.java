package wooteco.subway.path.domin;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public interface PathRepository {
    void generateAllPath(List<Line> lines);
    Path generateShortestDistancePath(Station start, Station destination);
}
