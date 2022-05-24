package wooteco.subway.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPathFactory {

    public static DijkstraShortestPath<Station, PathEdge> getFrom(Map<Section, Fare> edges) {
        WeightedMultigraph<Station, PathEdge> graph = getMultiGraph(edges);
        return new DijkstraShortestPath<>(graph);
    }

    private static WeightedMultigraph<Station, PathEdge> getMultiGraph(Map<Section, Fare> edges) {
        Set<Section> sections = edges.keySet();
        Set<Station> stations = extractStations(sections);
        WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            PathEdge edge = new PathEdge(edges.get(section), section.getDistance());
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
        }
        return graph;
    }

    private static Set<Station> extractStations(Set<Section> sections) {
        Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }
}
