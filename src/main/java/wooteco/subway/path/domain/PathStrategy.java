package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public interface PathStrategy {
    GraphPath<Station, DefaultWeightedEdge> calculateShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                    List<Station> stations, Station source, Station target);
}
