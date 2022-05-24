package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPathFactory {

    public static DijkstraShortestPath<Station, DefaultWeightedEdge> getFrom(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = getMultiGraph(sections);
        return new DijkstraShortestPath<>(graph);
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> getMultiGraph(List<Section> sections) {
        Set<Station> stations = extractStations(sections);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance()
            );
        }
        return graph;
    }

    private static Set<Station> extractStations(List<Section> sections) {
        Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }
}
