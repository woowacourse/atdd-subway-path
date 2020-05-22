package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;

public interface CustomShortestPathAlgorithm {
    GraphPath<Station, LineStationEdge> getPath(WeightedMultigraph<Station, LineStationEdge> graph,
                                                Station source, Station target);
}
