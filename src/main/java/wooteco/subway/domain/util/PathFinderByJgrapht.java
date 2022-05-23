package wooteco.subway.domain.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;

public class PathFinderByJgrapht implements PathFinder {

    private final List<Section> sections;

    public PathFinderByJgrapht(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    @Override
    public Path findShortestPath(Long source, Long target) {
        GraphPath<Long, SubwayWeightedEdge> path = initPathGraph(source, target, findStationIds());
        return new Path(path.getVertexList(), findLineIdsOfPath(path), (int) path.getWeight());
    }

    private Set<Long> findStationIds() {
        Set<Long> ids = new HashSet<>();
        for (Section section : sections) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    private GraphPath<Long, SubwayWeightedEdge> initPathGraph(Long source, Long target, Set<Long> ids) {
        WeightedMultigraph<Long, SubwayWeightedEdge> graph = new WeightedMultigraph(SubwayWeightedEdge.class);
        for (Long id : ids) {
            graph.addVertex(id);
        }
        for (Section section : sections) {
            SubwayWeightedEdge subwayWeightedEdge = graph.addEdge(section.getUpStationId(), section.getDownStationId());
            subwayWeightedEdge.setLineId(section.getLineId());
            graph.setEdgeWeight(subwayWeightedEdge, section.getDistance());
        }
        return new DijkstraShortestPath(graph).getPath(source, target);
    }

    private Set<Long> findLineIdsOfPath(final GraphPath<Long, SubwayWeightedEdge> path) {
        return path.getEdgeList().stream()
                .map(SubwayWeightedEdge::getLineId)
                .collect(Collectors.toSet());
    }
}
