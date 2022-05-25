package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface PathCalculator {
    GraphPath<Station, DefaultWeightedEdge> findShortestPath(final Station source, final Station target);
}
