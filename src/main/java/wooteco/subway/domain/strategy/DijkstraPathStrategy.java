package wooteco.subway.domain.strategy;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.ShortestPathEdge;
import wooteco.subway.exception.duplicate.DuplicateStationException;
import wooteco.subway.exception.invalidrequest.InvalidPathRequestException;

@Component
public class DijkstraPathStrategy implements PathStrategy {

    @Override
    public Path calculatePath(Station source, Station target, Sections sections) {
        validateSourceAndTargetNotSame(source, target);
        WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        addVertices(sections, graph);
        addEdges(sections, graph);
        DijkstraShortestPath<Station, ShortestPathEdge> shortestPath = new DijkstraShortestPath<>(graph);
        try {
            GraphPath<Station, ShortestPathEdge> graphPath = shortestPath.getPath(source, target);
            return new Path(graphPath.getVertexList(), findPassedLines(graphPath),
                    (int) shortestPath.getPathWeight(source, target));
        } catch (IllegalArgumentException e) {
            throw new InvalidPathRequestException("목적지까지 도달할 수 없습니다.");
        }
    }

    private void validateSourceAndTargetNotSame(Station source, Station target) {
        if (source.equals(target)) {
            throw new DuplicateStationException("경로의 시작과 끝은 같은 역일 수 없습니다.");
        }
    }

    private void addVertices(Sections sections, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Station station : sections.getDistinctStations()) {
            graph.addVertex(station);
        }
    }

    private void addEdges(Sections sections, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Section value : sections.getValues()) {
            ShortestPathEdge edge = new ShortestPathEdge(value.getLine(), value.getDistance());
            graph.addEdge(value.getUpStation(), value.getDownStation(), edge);
        }
    }

    private List<Line> findPassedLines(GraphPath<Station, ShortestPathEdge> graphPath) {
        return graphPath.getEdgeList()
                .stream()
                .map(ShortestPathEdge::getLine)
                .distinct()
                .collect(Collectors.toList());
    }

}
