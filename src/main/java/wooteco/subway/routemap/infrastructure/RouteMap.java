package wooteco.subway.routemap.infrastructure;

import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.graph.Multigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.path.domain.PathEdge;
import wooteco.subway.station.domain.Station;

@Component
public class RouteMap {

    private final Multigraph<Station, PathEdge> graph;

    public RouteMap(Multigraph<Station, PathEdge> graph) {
        this.graph = graph;
    }

    public void updateStations(Lines lines) {
        Set<Station> expectedStations = lines.toDistinctStations();
        removeStations(expectedStations);
        addStations(expectedStations);
    }

    private void removeStations(Set<Station> expectedStations) {
        graph.removeAllVertices(
            findRemovalTarget(expectedStations)
        );
    }

    private Set<Station> findRemovalTarget(Set<Station> expectedStations) {
        return leaveOnlyExistentInTarget(graph.vertexSet(), expectedStations);
    }

    private Set<Station> leaveOnlyExistentInTarget(Set<Station> targetStations, Set<Station> comparisonStations) {
        return targetStations.stream()
            .filter(station -> !comparisonStations.contains(station))
            .collect(Collectors.toSet());
    }

    private void addStations(Set<Station> expectedStations) {
        findAddingTarget(expectedStations)
            .forEach(graph::addVertex);
    }

    private Set<Station> findAddingTarget(Set<Station> expectedStations) {
        return leaveOnlyExistentInTarget(expectedStations, graph.vertexSet());
    }

    public Set<Station> toDistinctStations() {
        return graph.vertexSet();
    }
}
