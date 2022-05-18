package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathGraph {

    private PathGraph() {
    }

    public static DijkstraShortestPath<Station, DefaultWeightedEdge> create(final Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sections.getAllStations()) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            assignWeight(graph, section);
        }
        return new DijkstraShortestPath<>(graph);
    }

    private static void assignWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                     final Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }
}
