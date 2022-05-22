package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPathFactory {

    public static DijkstraShortestPath<Station, DefaultWeightedEdge> getFrom(List<Station> stations,
                                                                             List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = getMultiGraph(stations, sections);
        return new DijkstraShortestPath<>(graph);
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> getMultiGraph(List<Station> stations,
                                                                                  List<Section> sections) {
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
}
