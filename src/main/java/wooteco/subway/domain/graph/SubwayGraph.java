package wooteco.subway.domain.graph;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class SubwayGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public SubwayGraph(List<Section> sections) {
        this.graph = createGraph(sections);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initializeGraph(graph, sections);
        return graph;
    }

    private void initializeGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        addVertexes(graph, sections);
        addEdges(graph, sections);
    }

    private void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
        }
    }

    private void addEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance);
        }
    }

    public Route calculateShortestRoute(Station source, Station target) {
        validateSourceAndTargetExist(source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);

        if (Objects.isNull(path)) {
            throw new IllegalStateException("출발지부터 도착지까지 구간이 연결되어 있지 않습니다.");
        }

        List<Station> shortestPath = path.getVertexList();
        int pathDistance = (int) path.getWeight();
        return new Route(shortestPath, pathDistance);
    }

    private void validateSourceAndTargetExist(Station source, Station target) {
        if (isStationNotExist(source) || isStationNotExist(target)) {
            throw new IllegalArgumentException("출발지 또는 도착지에 대한 구간 정보가 없습니다");
        }
    }

    private boolean isStationNotExist(Station station) {
        return !graph.containsVertex(station);
    }
}
