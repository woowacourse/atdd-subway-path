package wooteco.subway.infrastructure.jgraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.domain.Id;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Distance;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.graph.SubwayGraph;

@Component
public class SubwayJGraph implements SubwayGraph {

    @Override
    public Path calculateShortestPath(List<Line> lines, long source, long target) {
        return calculateShortestPath(lines, new Id(source), new Id(target));
    }

    private Path calculateShortestPath(List<Line> lines, Id source, Id target) {
        WeightedMultigraph<Id, SubwayJGraphEdge> graph = initializeGraph(lines);

        GraphPath<Id, SubwayJGraphEdge> shortestPath = getShortestPath(graph, source, target);

        List<Id> passedStations = shortestPath.getVertexList();
        List<Line> passedLines = extractLines(shortestPath.getEdgeList());
        long pathDistance = (long) shortestPath.getWeight();
        return new Path(passedStations, passedLines, pathDistance);
    }

    private WeightedMultigraph<Id, SubwayJGraphEdge> initializeGraph(List<Line> lines) {
        WeightedMultigraph<Id, SubwayJGraphEdge> graph = new WeightedMultigraph<>(
                SubwayJGraphEdge.class);
        lines.forEach(line -> addVertexAndEdge(graph, line));
        return graph;
    }

    private void addVertexAndEdge(WeightedMultigraph<Id, SubwayJGraphEdge> graph, Line line) {
        for (Section section : line.getSections()) {
            Id upStationId = new Id(section.getUpStationId());
            Id downStationId = new Id(section.getDownStationId());
            Distance distance = new Distance(section.getDistance());

            graph.addVertex(upStationId);
            graph.addVertex(downStationId);
            graph.addEdge(upStationId, downStationId, new SubwayJGraphEdge(line, distance));
        }
    }

    private GraphPath<Id, SubwayJGraphEdge> getShortestPath(WeightedMultigraph<Id, SubwayJGraphEdge> graph,
                                                                       Id source, Id target) {
        validateSourceAndTargetExist(graph, source, target);
        return Optional.ofNullable((new DijkstraShortestPath<>(graph)).getPath(source, target))
                .orElseThrow(() -> new IllegalStateException("출발지부터 도착지까지 이어지는 경로가 존재하지 않습니다."));
    }

    private void validateSourceAndTargetExist(WeightedMultigraph<Id, SubwayJGraphEdge> graph,
                                              Id source, Id target) {
        if (isStationNotExist(graph, source) || isStationNotExist(graph, target)) {
            throw new IllegalArgumentException("출발지 또는 도착지에 연결된 구간이 존재하지 않습니다.");
        }
    }

    private boolean isStationNotExist(WeightedMultigraph<Id, SubwayJGraphEdge> graph, Id station) {
        return !graph.containsVertex(station);
    }

    private List<Line> extractLines(List<SubwayJGraphEdge> edges) {
        return edges.stream()
                .map(SubwayJGraphEdge::getLine)
                .collect(Collectors.toUnmodifiableList());
    }
}
