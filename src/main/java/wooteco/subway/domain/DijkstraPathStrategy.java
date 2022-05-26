package wooteco.subway.domain;

import java.util.*;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import org.springframework.stereotype.Component;
import wooteco.subway.exception.ClientException;

@Component
public class DijkstraPathStrategy implements PathStrategy {

    @Override
    public Path findPath(List<Section> sections, Long source, Long target) {
        GraphPath graph = initPathGraph(sections, gatherStationIds(sections), source, target);

        List<SectionWeightEdge> edges = graph.getEdgeList();
        List<Long> lineIds = edges.stream()
                .map(SectionWeightEdge::getLineId)
                .collect(Collectors.toList());

        return new Path(graph.getVertexList(), lineIds, (int)graph.getWeight());
    }

    private static Set<Long> gatherStationIds(List<Section> sections) {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }
        return stationIds;
    }

    private static GraphPath initPathGraph(List<Section> sections, Set<Long> ids, Long source, Long target) {
        WeightedMultigraph<Long, SectionWeightEdge> graph = new WeightedMultigraph(SectionWeightEdge.class);
        for (Long id : ids) {
            graph.addVertex(id);
        }

        for (Section section : sections) {
            graph.addEdge(section.getUpStationId(), section.getDownStationId(),
                    new SectionWeightEdge(section.getLineId(), section.getDistance()));
        }
        return makePath(source, target, graph);
    }

    private static GraphPath makePath(Long source, Long target, WeightedMultigraph<Long, SectionWeightEdge> graph) {
        try {
            GraphPath path = new DijkstraShortestPath(graph).getPath(source, target);
            validateImpossiblePath(path);
            return path;
        } catch (IllegalArgumentException exception) {
            throw new ClientException("구간에 등록되지 않은 역입니다.");
        }
    }

    private static void validateImpossiblePath(GraphPath path) {
        if (path == null) {
            throw new ClientException("갈 수 없는 경로입니다.");
        }
    }
}
