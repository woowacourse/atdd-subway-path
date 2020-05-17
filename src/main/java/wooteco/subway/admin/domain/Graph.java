package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Graph {
    WeightedMultigraph<Station, Edge> graph;

    private Graph(WeightedMultigraph<Station, Edge> graph) {
        this.graph = graph;
    }

    public static Graph of(List<Line> lines, List<Station> stations, EdgeType type) {
        WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph<>(Edge.class);
        stations.forEach(station -> graph.addVertex(station));

        lines.stream()
                .flatMap(line -> line.getLineStations().stream())
                .filter(lineStation -> lineStation.getPreStationId() != null)
                .forEach(lineStation -> {
                    Edge edge = Edge.of(lineStation);
                    graph.addEdge(findStationById(lineStation.getPreStationId(), stations),
                            findStationById(lineStation.getStationId(), stations),
                            edge);
                    graph.setEdgeWeight(edge, type.getEdgeValue(lineStation));
                });
        return new Graph(graph);
    }

    private static Station findStationById(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.getId().equals(stationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }

    public GraphPath<Station, Edge> findShortestPath(Station startStation, Station targetStation) {
        DijkstraShortestPath<Station, Edge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, Edge> path = dijkstraShortestPath.getPath(startStation, targetStation);
        return path;
    }
}
