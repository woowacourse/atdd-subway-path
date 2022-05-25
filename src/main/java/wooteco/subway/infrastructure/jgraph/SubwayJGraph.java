package wooteco.subway.infrastructure.jgraph;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.domain.Id;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Distance;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.SubwayGraph;

@Component
public class SubwayJGraph implements SubwayGraph {

    @Override
    public Path calculateShortestPath(List<Line> lines, long source, long target) {
        return calculateShortestPath(lines, new Id(source), new Id(target));
    }

    private Path calculateShortestPath(List<Line> lines, Id source, Id target) {
        WeightedMultigraph<Id, DefaultWeightedEdge> graph = initializeGraph(lines);
        validateSourceAndTargetExist(graph, source, target);

        GraphPath<Id, DefaultWeightedEdge> path = (new DijkstraShortestPath<>(graph)).getPath(source, target);
        validatePathExist(path);

        List<Id> shortestPath = path.getVertexList();
        Distance pathDistance = new Distance((long) path.getWeight());
        return new Path(shortestPath, pathDistance);
    }

    private WeightedMultigraph<Id, DefaultWeightedEdge> initializeGraph(List<Line> lines) {
        WeightedMultigraph<Id, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        lines.forEach(line -> addVertexAndEdge(graph, line));
        return graph;
    }

    private void addVertexAndEdge(WeightedMultigraph<Id, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            Id upStationId = new Id(section.getUpStationId());
            Id downStationId = new Id(section.getDownStationId());
            long distance = section.getDistance();

            graph.addVertex(upStationId);
            graph.addVertex(downStationId);
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), distance);
        }
    }

    private void validateSourceAndTargetExist(WeightedMultigraph<Id, DefaultWeightedEdge> graph,
                                              Id source, Id target) {
        if (isStationNotExist(graph, source) || isStationNotExist(graph, target)) {
            throw new IllegalArgumentException("출발지 또는 도착지에 연결된 구간이 존재하지 않습니다.");
        }
    }

    private boolean isStationNotExist(WeightedMultigraph<Id, DefaultWeightedEdge> graph, Id station) {
        return !graph.containsVertex(station);
    }

    private void validatePathExist(GraphPath<Id, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalStateException("출발지부터 도착지까지 이어지는 경로가 존재하지 않습니다.");
        }
    }
}
