package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathCalculator {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private PathCalculator(
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathCalculator from(List<Section> sections) {
        Set<Station> stations = extractStations(sections);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = getMultiGraph(stations, sections);
        return new PathCalculator(new DijkstraShortestPath<>(graph));
    }

    private static Set<Station> extractStations(List<Section> sections) {
        Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> getMultiGraph(Set<Station> stations,
            List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(stations, graph);
        addEdge(sections, graph);
        return graph;
    }

    private static void addVertex(Set<Station> stations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public List<Station> calculateShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public double calculateShortestDistance(Station source, Station target) {
        return dijkstraShortestPath.getPathWeight(source, target);
    }
}
