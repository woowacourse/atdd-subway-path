package wooteco.subway.domain.path.strategy;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class DijkstraStrategy implements PathFindingStrategy {
    public GraphPath<Station, Section> findPathBetween(Graph<Station, Section> graph, Station source, Station target) {
        return DijkstraShortestPath.findPathBetween(graph, source, target);
    }
}
