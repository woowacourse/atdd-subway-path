package wooteco.subway.domain.strategy.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.PathNotFoundException;

@Component
public class PathFindDijkstraStrategy implements PathFindStrategy {

    @Override
    public Path calculatePath(Station source, Station target, Sections sections) {
        validateSameStation(source, target);

        WeightedMultigraph<Station, ShortestPathCustomEdge> graph = new WeightedMultigraph<>(ShortestPathCustomEdge.class);
        addVertex(sections, graph);
        addEdge(sections, graph);

        GraphPath<Station, ShortestPathCustomEdge> shortPath = createShortestPath(source, target, graph);
        List<Long> distinctLineIds = createDistinctLineIds(shortPath);

        return new Path(shortPath.getVertexList(), distinctLineIds, (int)shortPath.getWeight());
    }

    private GraphPath<Station, ShortestPathCustomEdge> createShortestPath(
            Station source, Station target, WeightedMultigraph<Station , ShortestPathCustomEdge> graph) {

        DijkstraShortestPath<Station, ShortestPathCustomEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            return dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathNotFoundException(source, target);
        }
    }

    private List<Long> createDistinctLineIds(GraphPath<Station, ShortestPathCustomEdge> shortPath) {
        return shortPath.getEdgeList().stream()
                .map(value -> value.getLineId())
                .distinct()
                .collect(Collectors.toList());
    }

    private void addVertex(Sections sections, WeightedMultigraph<Station, ShortestPathCustomEdge> graph) {
        for (Station station : sections.getDistinctStations()) {
            graph.addVertex(station);
        }
    }

    private void addEdge(Sections sections, WeightedMultigraph<Station, ShortestPathCustomEdge> graph) {
        for (Section section : sections.getValues()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new ShortestPathCustomEdge(section.getLineId(), section.getDistance()));
        }
    }

    private void validateSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }
}
