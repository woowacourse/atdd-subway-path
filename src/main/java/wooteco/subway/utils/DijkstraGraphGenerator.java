package wooteco.subway.utils;

import java.util.HashSet;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class DijkstraGraphGenerator {

    public static DijkstraShortestPath<Station, DefaultWeightedEdge> createSectionDijkstraShortestPath(
            final Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : findAllStationByDistinct(sections)) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            assignWeight(graph, section);
        }
        return new DijkstraShortestPath<>(graph);
    }

    private static Set<Station> findAllStationByDistinct(final Sections sections) {
        Set<Station> stations = new HashSet<>();
        stations.addAll(sections.getUpStations());
        stations.addAll(sections.getDownStations());
        return stations;
    }

    private static void assignWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
            final Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }
}
