package wooteco.subway.path.infrastructure;

import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public abstract class ShortestPathGraph implements ShortestPath {
    protected final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public ShortestPathGraph(
        WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        this.graph = graph;
        for (
            Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
