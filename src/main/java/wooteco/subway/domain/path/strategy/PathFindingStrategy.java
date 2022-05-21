package wooteco.subway.domain.path.strategy;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import wooteco.subway.domain.path.PathEdge;
import wooteco.subway.domain.station.Station;

public interface PathFindingStrategy {
    GraphPath<Station, PathEdge> findPathBetween(Graph<Station, PathEdge> graph, Station source, Station target);
}
