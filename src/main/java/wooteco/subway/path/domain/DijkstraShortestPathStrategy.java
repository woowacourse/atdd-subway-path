package wooteco.subway.path.domain;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class DijkstraShortestPathStrategy extends ShortestPathStrategy {
    public ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(List<Line> lines) {
        return new DijkstraShortestPath<>(new PathGraph(lines).graph());
    }
}
