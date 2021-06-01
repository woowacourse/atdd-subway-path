package wooteco.subway.path.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Graph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Graph(List<Section> sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        resetGraph(sections);
    }

    private void resetGraph(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

}
