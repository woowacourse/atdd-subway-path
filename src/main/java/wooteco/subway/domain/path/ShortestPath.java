package wooteco.subway.domain.path;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;

public class ShortestPath implements PathAlgorithm {
    private final DijkstraShortestPath<Station, PathEdge> dijkstraShortestPath;

    public ShortestPath(Map<Section, Fare> edges) {
        WeightedMultigraph<Station, PathEdge> graph = getMultiGraph(edges);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private static WeightedMultigraph<Station, PathEdge> getMultiGraph(Map<Section, Fare> edges) {
        WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        addVertexes(edges.keySet(), graph);
        addEdges(edges, graph);
        return graph;
    }

    private static void addVertexes(Set<Section> sections, WeightedMultigraph<Station, PathEdge> graph) {
        Set<Station> stations = extractStations(sections);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static Set<Station> extractStations(Set<Section> sections) {
        Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }

    private static void addEdges(Map<Section, Fare> edges, WeightedMultigraph<Station, PathEdge> graph) {
        Set<Section> sections = edges.keySet();
        for (Section section : sections) {
            PathEdge edge = new PathEdge(edges.get(section), section.getDistance());
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
        }
    }

    @Override
    public Path getPath(Station source, Station target) {
        checkStations(source, target);
        try {
            GraphPath<Station, PathEdge> path = dijkstraShortestPath.getPath(source, target);
            checkPath(path);
            return new Path(path.getVertexList(), Distance.fromKilometer(path.getWeight()), getExtraFare(path));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("해당 역은 경로에 존재하지 않습니다.");
        }
    }

    private void checkStations(Station source, Station target) {
        checkNull(source, target);
        checkEquals(source, target);
    }

    private void checkNull(Station source, Station target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 모두 필수입니다.");
        }
    }

    private void checkEquals(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같아 경로를 찾을 수 없습니다.");
        }
    }

    private void checkPath(GraphPath<Station, ? extends DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalStateException("해당하는 경로가 존재하지 않습니다.");
        }
    }

    private Fare getExtraFare(GraphPath<Station, PathEdge> path) {
        return path.getEdgeList().stream()
                .map(PathEdge::getExtraFare)
                .max(Fare::compareTo)
                .orElse(new Fare(0));
    }
}
