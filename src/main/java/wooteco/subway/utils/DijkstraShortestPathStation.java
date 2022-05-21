package wooteco.subway.utils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class DijkstraShortestPathStation {

    public static Path getPath(Sections sections, Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = getDijkstraShortestPath(sections);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        if (graphPath == null) {
            throw new NoSuchElementException("이동할 수 있는 경로가 없습니다.");
        }

        List<Station> visitStations = getVisitStations(graphPath);
        int distance = getDistance(graphPath);

        return new Path(visitStations, distance);
    }

    private static DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath(
            Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sections.getStations()) {
            graph.addVertex(station);
        }
        sections.forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );

        return new DijkstraShortestPath<>(graph);
    }

    private static Station getStation(List<Station> stations, long stationId) {
        return stations.stream()
                .filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("역이 없습니다."));
    }

    private static List<Station> getVisitStations(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return graphPath.getVertexList().stream()
                .map(station -> new Station(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private static int getDistance(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return (int) graphPath.getWeight();
    }
}
