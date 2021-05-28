package wooteco.subway.path.domain.strategy.shortestpath;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public abstract class ShortestPathStrategy {
    public abstract List<Station> getVertexList(List<Line> lines, Station source, Station target);

    public abstract int getWeight(List<Line> lines, Station source, Station target);
}
