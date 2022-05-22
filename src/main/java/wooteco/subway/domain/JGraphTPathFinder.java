package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.exception.notfound.NotFoundPathException;
import wooteco.subway.exception.notfound.NotFoundStationException;

@Component
public class JGraphTPathFinder implements PathFinder {

    JGraphTPathFinder() {
    }

    @Override
    public Path searchShortestPath(List<Section> sections, Station source, Station target) {
        List<Station> stations = extractStations(sections);
        validateSourceAndTargetExist(stations, source, target);

        final Optional<Path> shortestPath = findShortestPath(stations, sections, source, target);

        return shortestPath.orElseThrow(() -> new NotFoundPathException(source.getId(), target.getId()));
    }

    private List<Station> extractStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    private void validateSourceAndTargetExist(List<Station> stations, Station source, Station target) {
        if (!stations.contains(source)) {
            throw new NotFoundStationException(source.getId());
        }

        if (!stations.contains(target)) {
            throw new NotFoundStationException(target.getId());
        }
    }

    private Optional<Path> findShortestPath(List<Station> stations, List<Section> sections,
                                            Station source, Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = setupPath(stations, sections, source, target);

        if (Objects.isNull(path)) {
            return Optional.empty();
        }

        return Optional.of(new Path(path.getVertexList(), (int) path.getWeight()));
    }

    private GraphPath<Station, DefaultWeightedEdge> setupPath(List<Station> stations, List<Section> sections,
                                                              Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStations(stations, graph);
        addSections(sections, graph);

        return new DijkstraShortestPath<>(graph).getPath(source, target);
    }

    private void addStations(List<Station> stations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addSections(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
