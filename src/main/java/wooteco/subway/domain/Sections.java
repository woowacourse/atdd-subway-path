package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.util.SubwayWeightedEdge;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Path findShortestPath(Long source, Long target) {
        WeightedMultigraph<Long, SubwayWeightedEdge> graph = new WeightedMultigraph(SubwayWeightedEdge.class);
        initPathGraph(graph, gatherStationIds());
        GraphPath<Long, SubwayWeightedEdge> path = new DijkstraShortestPath(graph).getPath(source, target);
        Set<Long> lineIds = path.getEdgeList().stream()
                .map(SubwayWeightedEdge::getLineId)
                .collect(Collectors.toSet());
        return new Path(path.getVertexList(), lineIds, (int) path.getWeight());
    }

    private Set<Long> gatherStationIds() {
        Set<Long> ids = new HashSet<>();
        for (Section section : sections) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    private void initPathGraph(WeightedMultigraph<Long, SubwayWeightedEdge> graph, Set<Long> ids) {
        for (Long id : ids) {
            graph.addVertex(id);
        }
        for (Section section : sections) {
            SubwayWeightedEdge subwayWeightedEdge = graph.addEdge(section.getUpStationId(), section.getDownStationId());
            subwayWeightedEdge.setLineId(section.getLineId());
            graph.setEdgeWeight(subwayWeightedEdge, section.getDistance());
        }
    }
}
