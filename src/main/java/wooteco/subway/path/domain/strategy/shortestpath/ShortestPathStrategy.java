package wooteco.subway.path.domain.strategy.shortestpath;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public abstract class ShortestPathStrategy {
    public abstract ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(List<Line> lines);
}
