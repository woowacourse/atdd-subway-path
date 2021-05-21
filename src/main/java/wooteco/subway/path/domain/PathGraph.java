package wooteco.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.section.domain.Section;

import java.util.List;

public class PathGraph {

    private WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph;

    public PathGraph(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, List<Section> sections) {
        sections.forEach(section -> addSection(pathGraph, section));
        this.pathGraph = pathGraph;
    }

    private void addSection(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, Section section) {
        Long upStationId = section.getUpStation().getId();
        Long downStationId = section.getDownStation().getId();
        pathGraph.addVertex(upStationId);
        pathGraph.addVertex(downStationId);
        DefaultWeightedEdge defaultWeightedEdge = pathGraph.addEdge(upStationId, downStationId);
        pathGraph.setEdgeWeight(defaultWeightedEdge, section.getDistance());
    }

    public WeightedMultigraph<Long, DefaultWeightedEdge> getPathGraph() {
        return pathGraph;
    }
}
