package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Component
public class DijkstraStrategy implements PathFindStrategy {

    @Override
    public Path findPath(List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        initializeGraph(lines, graph);
        GraphPath<Station, PathEdge> path = new DijkstraShortestPath(graph).getPath(source, target);
        validatePath(path);
        return new Path(path.getVertexList(), (int) path.getWeight(), findUsedLines(path, lines));
    }

    private void initializeGraph(List<Line> lines, WeightedMultigraph<Station, PathEdge> graph) {
        for (Line line : lines) {
            initVertex(graph, line);
            initEdge(graph, line);
        }
    }

    private void initEdge(WeightedMultigraph<Station, PathEdge> graph, Line line) {
        for (Section section : line.getSections().getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    PathEdge.from(section));
        }
    }

    private void initVertex(WeightedMultigraph<Station, PathEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private void validatePath(GraphPath<Station, PathEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }

    private Lines findUsedLines(GraphPath<Station, PathEdge> path, List<Line> lines) {
        List<PathEdge> edges = path.getEdgeList();
        return new Lines(lines.stream()
                .filter(it -> isUsed(it, edges))
                .collect(Collectors.toUnmodifiableList()));
    }

    private boolean isUsed(Line line, List<PathEdge> edges) {
        return edges.stream()
                .anyMatch(it -> it.getLineId() == line.getId());
    }
}
