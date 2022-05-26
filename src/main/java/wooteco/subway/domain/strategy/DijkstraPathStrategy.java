package wooteco.subway.domain.strategy;

import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.ShortestPathEdge;
import wooteco.subway.exception.duplicate.DuplicateStationException;
import wooteco.subway.exception.invalidrequest.InvalidPathRequestException;

public class DijkstraPathStrategy implements PathStrategy {

    @Override
    public Path calculatePath(Station source, Station target, Sections sections) {
        validateSourceAndTargetNotSame(source, target);
        WeightedMultigraph<Station, ShortestPathEdge> graph = createGraph(sections);
        return createPath(source, target, graph);
    }

    private Path createPath(Station source, Station target, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        DijkstraShortestPath<Station, ShortestPathEdge> shortestPath = new DijkstraShortestPath<>(graph);
        try {
            GraphPath<Station, ShortestPathEdge> graphPath = shortestPath.getPath(source, target);
            Stations passedStations = new Stations(graphPath.getVertexList());
            Lines passedLines = findPassedLines(graphPath);
            int distance = (int) shortestPath.getPathWeight(source, target);

            return new Path(passedStations, passedLines, distance);
        } catch (IllegalArgumentException e) {
            throw new InvalidPathRequestException("목적지까지 도달할 수 없습니다.");
        }
    }

    private void validateSourceAndTargetNotSame(Station source, Station target) {
        if (source.equals(target)) {
            throw new DuplicateStationException("경로의 시작과 끝은 같은 역일 수 없습니다.");
        }
    }

    private WeightedMultigraph<Station, ShortestPathEdge> createGraph(Sections sections) {
        WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        addVertices(sections, graph);
        addEdges(sections, graph);
        return graph;
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

    private Lines findPassedLines(GraphPath<Station, ShortestPathEdge> graphPath) {
        return new Lines(
                graphPath.getEdgeList()
                        .stream()
                        .map(ShortestPathEdge::getLine)
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

}
