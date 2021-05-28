package wooteco.subway.path.domain.strategy.shortestpath;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.PathGraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class DijkstraShortestPathStrategy extends ShortestPathStrategy {
    @Override
    public List<Station> getVertexList(List<Line> lines, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = graphPath(lines, source, target);
        return graphPath.getVertexList();
    }

    @Override
    public int getWeight(List<Line> lines, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = graphPath(lines, source, target);
        return (int) graphPath.getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> graphPath(List<Line> lines, Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(new PathGraph(lines).graph());
        return dijkstraShortestPath.getPath(source, target);
    }
}
