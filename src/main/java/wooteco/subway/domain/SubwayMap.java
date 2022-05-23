package wooteco.subway.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.IllegalInputException;
import wooteco.subway.exception.path.NoSuchPathException;

public class SubwayMap {

    private final List<Line> lines;

    public SubwayMap(final List<Line> lines) {
        this.lines = lines;
    }

    public List<Station> searchPath(final Station sourceStation, final Station targetStation) {
        final GraphPath<Station, ShortestPathEdge> shortestPath = searchShortestPath(sourceStation, targetStation);
        return shortestPath.getVertexList();
    }

    private GraphPath<Station, ShortestPathEdge> searchShortestPath(final Station sourceStation,
                                                                    final Station targetStation) {
        validateStations(sourceStation, targetStation);

        final GraphPath<Station, ShortestPathEdge> shortestPath = toShortestPath()
                .getPath(sourceStation, targetStation);

        if (shortestPath == null) {
            throw new NoSuchPathException();
        }
        return shortestPath;
    }

    private void validateStations(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalInputException("출발역과 도착역이 동일합니다.");
        }
    }

    private DijkstraShortestPath<Station, ShortestPathEdge> toShortestPath() {
        final WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        for (Line line : lines) {
            addVertexAndEdge(graph, line);
        }

        return new DijkstraShortestPath<>(graph);
    }

    private void addVertexAndEdge(final WeightedMultigraph<Station, ShortestPathEdge> graph, final Line line) {
        for (Section section : toSectionList(line)) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new ShortestPathEdge(line, section.getDistance()));
        }
    }

    private List<Section> toSectionList(final Line line) {
        return line.getSections().getValue();
    }

    public Distance searchDistance(final Station sourceStation, final Station targetStation) {
        final GraphPath<Station, ShortestPathEdge> shortestPath = searchShortestPath(sourceStation, targetStation);
        return new Distance((int) shortestPath.getWeight());
    }

    public int searchExtraFareOfPath(final Station sourceStation, final Station targetStation) {
        Set<Line> linesOfPath = searchLinesOfPath(sourceStation, targetStation);
        return linesOfPath.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow();
    }

    private Set<Line> searchLinesOfPath(final Station sourceStation, final Station targetStation) {
        final GraphPath<Station, ShortestPathEdge> shortestPath = searchShortestPath(sourceStation, targetStation);
        return shortestPath.getEdgeList()
                .stream()
                .map(ShortestPathEdge::getLine)
                .collect(Collectors.toSet());
    }
}
