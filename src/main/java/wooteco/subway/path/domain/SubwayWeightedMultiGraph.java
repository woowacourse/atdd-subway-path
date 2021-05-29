package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.UnmodifiableUndirectedGraph;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class SubwayWeightedMultiGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph;

    public SubwayWeightedMultiGraph(List<Section> sections) {
        this.subwayGraph = subwayGraph(sections);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph =
            new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        for (Section section : sections) {
            subwayGraph.addVertex(section.getUpStation());
            subwayGraph.addVertex(section.getDownStation());

            subwayGraph.setEdgeWeight(subwayGraph.addEdge(
                section.getUpStation(), section.getDownStation()),
                section.getDistance());
        }
        return subwayGraph;
    }

    public UnmodifiableUndirectedGraph<Station, DefaultWeightedEdge> getGraph() {
        return new UnmodifiableUndirectedGraph<Station, DefaultWeightedEdge>(subwayGraph);
    }

}
