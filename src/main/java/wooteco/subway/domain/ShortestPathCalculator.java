package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.SectionNotFoundException;

public class ShortestPathCalculator {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> algorithm;

    public ShortestPathCalculator(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                  final ShortestPathAlgorithm<Station, DefaultWeightedEdge> algorithm) {
        this.graph = graph;
        this.algorithm = algorithm;
    }

    public void initializeGraph(final Sections sections) {
        for (Station station : sections.getAllStations()) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            assignWeight(section);
        }
    }

    private void assignWeight(final Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public int calculateShortestDistance(final Station startStation, final Station endStation) {
        try {
            return (int) algorithm.getPath(startStation, endStation).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public List<Station> calculateShortestStations(final Station startStation, final Station endStation) {
        try {
            return algorithm.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }
}
