package wooteco.subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionSeries;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Path {
    private final GraphPath<Station, WeightedEdgeWithLine> path;

    public Path(GraphPath<Station, WeightedEdgeWithLine> path) {
        this.path = path;
    }

    public static Path of(List<Line> lines, Station source, Station target) {
        validateDistinctStation(source, target);
        GraphPath<Station, WeightedEdgeWithLine> path = generatePath(lines, source, target);
        validatePath(path);

        return new Path(path);
    }

    public List<Station> findShortestPath() {
        return path.getVertexList();
    }

    public int findDistance() {
        return (int) Math.round(path.getWeight());
    }

    public Set<Long> findUsedLineId() {
        return path.getEdgeList().stream()
                .map(WeightedEdgeWithLine::getLineId)
                .collect(Collectors.toSet());
    }

    private static void validateDistinctStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathNotFoundException("출발지와 도착지는 동일할 수 없습니다.");
        }
    }

    private static GraphPath<Station, WeightedEdgeWithLine> generatePath(List<Line> lines, Station source, Station target) {
        try {
            WeightedMultigraph<Station, WeightedEdgeWithLine> graph = generateGraph(lines);
            return new DijkstraShortestPath<>(graph).getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathNotFoundException("노선에 등록되지 않은 역의 경로는 조회할 수 없습니다.");
        }
    }

    private static WeightedMultigraph<Station, WeightedEdgeWithLine> generateGraph(List<Line> lines) {
        WeightedMultigraph<Station, WeightedEdgeWithLine> graph = new WeightedMultigraph<>(WeightedEdgeWithLine.class);

        for (Line line : lines) {
            addSection(graph, line.getId(), line.getSectionSeries());
        }

        return graph;
    }

    private static void addSection(WeightedMultigraph<Station, WeightedEdgeWithLine> graph, Long lindId, SectionSeries sectionSeries) {
        for (Section section : sectionSeries.getSections()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.addEdge(
                    section.getUpStation(), section.getDownStation(),
                    new WeightedEdgeWithLine(lindId, section.getDistance().getValue())
            );
        }
    }

    private static void validatePath(GraphPath<Station, WeightedEdgeWithLine> path) {
        if (path == null) {
            throw new PathNotFoundException("경로가 존재하지 않습니다.");
        }
    }
}
