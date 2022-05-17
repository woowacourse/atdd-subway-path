package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.dto.PathDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public PathDto findShortestPath(Long source, Long target) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initPathGraph(graph, gatherStationIds());
        GraphPath path = new DijkstraShortestPath(graph).getPath(source, target);
        return new PathDto(path.getVertexList(), (int) path.getWeight());
    }

    private Set<Long> gatherStationIds() {
        Set<Long> ids = new HashSet<>();
        for (Section section : sections) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    private void initPathGraph(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Set<Long> ids) {
        for (Long id : ids) {
            graph.addVertex(id);
        }

        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }
    }
}
