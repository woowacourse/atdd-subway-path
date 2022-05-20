package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Path {

    private static final String NOT_EXIST_STATION = "출발지, 도착지 모두 존재해야 됩니다.";
    private static final String NO_REACHABLE = "출발지에서 도착지로 갈 수 없습니다.";

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    private final Stations stations;

    public Path(Stations stations, Sections sections) {
        this.stations = stations;
        addVertexToGraph(stations);
        addEdgeToGraph(sections);
    }

    private void addEdgeToGraph(Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }
    }

    private void addVertexToGraph(Stations stations) {
        for (Station station : stations.getStations()) {
            graph.addVertex(station.getId());
        }
    }

    public List<Station> calculateShortestPath(long source, long target) {
        Optional<GraphPath<Long, DefaultWeightedEdge>> path = makeGraphPath(source, target);

        return path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE))
                .getVertexList()
                .stream()
                .map(stations ::getStationById)
                .collect(Collectors.toList());
    }

    public int calculateShortestDistance(Long source, Long target) {
        Optional<GraphPath<Long, DefaultWeightedEdge>> path = makeGraphPath(source, target);

        return (int) path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE)).getWeight();
    }

    private Optional<GraphPath<Long, DefaultWeightedEdge>> makeGraphPath(Long source, Long target) {
        Optional<GraphPath<Long, DefaultWeightedEdge>> path;

        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            path = Optional.ofNullable(
                    dijkstraShortestPath.getPath(source, target));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(NOT_EXIST_STATION);
        }

        return path;
    }
}
