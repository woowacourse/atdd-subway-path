package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
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
        GraphPath<Station, DefaultWeightedEdge> path = shortestPathStrategy.match(lines).getPath(source, target);
        return path.getVertexList();
    }

    public int shortestDistance(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = shortestPathStrategy.match(lines).getPath(source, target);
        return (int) path.getWeight();
    }
}
