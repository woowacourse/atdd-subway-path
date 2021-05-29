package wooteco.subway.path.domain.strategy.shortestpath;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public abstract class ShortestPathStrategy {
    public List<Station> getVertexList(List<Line> lines, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = graphPath(lines, source, target);
        return graphPath.getVertexList();
    }

    public int getWeight(List<Line> lines, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = graphPath(lines, source, target);
        return (int) graphPath.getWeight();
    }

    public abstract GraphPath<Station, DefaultWeightedEdge> graphPath(List<Line> lines, Station source, Station target);
}
