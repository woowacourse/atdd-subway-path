package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;


public interface PathFinder {

    SubwayPath findPath(Station source, Station target);

    void updateGraph(List<Line> lines);
}
