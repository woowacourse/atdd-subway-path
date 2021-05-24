package wooteco.subway.routemap.infrastructure;

import java.util.LinkedHashSet;
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

    public void addStation(Station station) {
        graph.addVertex(station);
    }

    public void removeStation(Station station) {
        graph.removeVertex(station);
    }

    public void updateStations(Set<Station> expectedStations) {
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

    public void updateSections(Lines lines) {
        clearSection();
        addSections(lines);
    }

    private void clearSection() {
        graph.removeAllEdges(new LinkedHashSet<>(graph.edgeSet()));
    }

    private void addSections(Lines lines) {
        lines.toAllSections().forEach(
            section -> graph.addEdge(
                section.getUpStation(),
                section.getDownStation(),
                new PathEdge(section, lines.findLineBySectionContaining(section))
            )
        );
    }

    public Set<Station> toDistinctStations() {
        return graph.vertexSet();
    }

    public Set<PathEdge> toPathEdges() {
        return graph.edgeSet();
    }
}
