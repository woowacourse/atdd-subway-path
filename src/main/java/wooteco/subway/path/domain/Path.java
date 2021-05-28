package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.strategy.shortestpath.ShortestPathStrategy;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final ShortestPathStrategy shortestPathStrategy;
    private final List<Line> lines;

    public Path(ShortestPathStrategy shortestPathStrategy, List<Line> lines) {
        this.shortestPathStrategy = shortestPathStrategy;
        this.lines = lines;
    }

    public List<Station> shortestPath(Station source, Station target) {
        return shortestPathStrategy.getVertexList(lines, source, target);
    }

    public int shortestDistance(Station source, Station target) {
        return shortestPathStrategy.getWeight(lines, source, target);
    }
}
