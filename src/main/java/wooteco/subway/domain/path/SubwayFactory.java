package wooteco.subway.domain.path;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;

import java.util.List;

public class SubwayFactory {
    public static Graph<Long, PathEdge> from(List<Section> sections) {
        WeightedMultigraph<Long, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

        for (Section section : sections) {
            graph.addVertex(section.getUpStationId());
            graph.addVertex(section.getDownStationId());

            PathEdge pathEdge = new PathEdge(section);
            graph.addEdge(section.getUpStationId(), section.getDownStationId(), pathEdge);
        }

        return graph;
    }
}
