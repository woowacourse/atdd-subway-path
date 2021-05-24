package wooteco.subway.path.domain.strategy.shortestpath;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.PathGraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class FloydWarshallShortestPathStrategy {
    public ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(List<Line> lines) {
        return new FloydWarshallShortestPaths<>(new PathGraph(lines).graph());
    }
}
