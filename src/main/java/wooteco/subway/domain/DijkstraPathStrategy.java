package wooteco.subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return new Path(graph.getEdgeList(), (int)graph.getWeight());
    }

    private static Set<Long> gatherStationIds(List<Section> sections) {
        Set<Long> ids = new HashSet<>();
        for (Section section : sections) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    private static GraphPath initPathGraph(List<Section> sections, Set<Long> ids, Long source, Long target) {
        WeightedMultigraph<Long, SectionWeightEdge> graph = new WeightedMultigraph(SectionWeightEdge.class);
        for (Long id : ids) {
            graph.addVertex(id);
        }

        for (Section section : sections) {
            graph.addEdge(section.getUpStationId(), section.getDownStationId(),
                    new SectionWeightEdge(section.getLineId(), section.getUpStationId(), section.getDownStationId(), section.getDistance()));
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
