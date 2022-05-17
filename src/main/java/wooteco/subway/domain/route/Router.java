package wooteco.subway.domain.route;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Router {

    public Optional<Route> findShortestRoute(List<Section> sections, Station source, Station target) {
        List<Station> stations = extractStations(sections);

        validateSourceAndTargetExist(stations, source, target);

        return findShortestRoute(stations, sections, source, target);
    }

    private List<Station> extractStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    private void validateSourceAndTargetExist(List<Station> stations, Station source, Station target) {
        if (!stations.contains(source) || !stations.contains(target)) {
            throw new IllegalArgumentException("출발지 또는 도착지에 대한 구간 정보가 없습니다");
        }
    }

    private Optional<Route> findShortestRoute(List<Station> stations, List<Section> sections,
                                              Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStations(stations, graph);
        addSections(sections, graph);

        DijkstraShortestPath<Station, Integer> dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, Integer> path = dijkstraShortestPath.getPath(source, target);

        if (Objects.isNull(path)) {
            return Optional.empty();
        }

        List<Station> shortestPath = path.getVertexList();
        double pathDistance = path.getWeight();
        return Optional.of(new Route(shortestPath, pathDistance));
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
