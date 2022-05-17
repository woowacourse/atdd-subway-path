package wooteco.subway.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.dto.StationResponse;

public class PathFinder {

    public static class Path {

        private List<Station> path;
        private int distance;
        private int fare;

        public Path(List<Station> path, int distance, int fare) {
            this.path = path;
            this.distance = distance;
            this.fare = fare;
        }

        public List<Station> getPath() {
            return path;
        }

        public int getDistance() {
            return distance;
        }

        public int getFare() {
            return fare;
        }
    }

    public static Path find(List<Station> stations,
                            List<Section> sections,
                            Long source, Long target) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station.getId());
        }

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                section.getDistance());
        }

        GraphPath<Long, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph)
            .getPath(source, target);

        Map<Long, Station> map = stations.stream()
            .collect(Collectors.toMap(Station::getId, value -> value));

        List<Station> stationList = path.getVertexList().stream()
            .map(map::get)
            .collect(Collectors.toList());

        int distance = (int) path.getWeight();
        int fare = 1250;

        return new Path(stationList, distance, fare);
    }
}
