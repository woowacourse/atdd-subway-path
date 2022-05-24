package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinderByJgrapht implements PathFinder {

    @Override
    public Path findShortestPath(List<Section> sections, Long source, Long target) {
        GraphPath<Long, SubwayWeightedEdge> path = initPathGraph(sections, source, target, findStationIds(sections));
        return new Path(path.getVertexList(), findLineIdsOfPath(path), (int) path.getWeight());
    }

    private Set<Long> findStationIds(List<Section> sections) {
        Set<Long> ids = new HashSet<>();
        for (Section section : sections) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    private GraphPath<Long, SubwayWeightedEdge> initPathGraph(List<Section> sections,
                                                              Long source,
                                                              Long target,
                                                              Set<Long> ids) {
        WeightedMultigraph<Long, SubwayWeightedEdge> graph = new WeightedMultigraph(SubwayWeightedEdge.class);
        for (Long id : ids) {
            graph.addVertex(id);
        }
        for (Section section : sections) {
            SubwayWeightedEdge subwayWeightedEdge = new SubwayWeightedEdge(section.getLineId());
            graph.addEdge(section.getUpStationId(), section.getDownStationId(), subwayWeightedEdge);
            graph.setEdgeWeight(subwayWeightedEdge, section.getDistance());
        }
        return new DijkstraShortestPath(graph).getPath(source, target);
    }

    private List<Long> findLineIdsOfPath(final GraphPath<Long, SubwayWeightedEdge> path) {
        return path.getEdgeList().stream()
                .map(SubwayWeightedEdge::getLineId)
                .collect(Collectors.toList());
    }
}
