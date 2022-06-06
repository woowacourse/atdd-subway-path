package wooteco.subway.domain.path;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Lines;

@Component
public class DijkstraPathCalculator implements PathCalculator {

    @Override
    public Path calculatePath(Station source, Station target, List<Line> inputLines) {
        Lines lines = new Lines(inputLines);
        Set<Station> stations = lines.extractStations();
        WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        addVertexes(stations, graph);
        addEdges(lines, graph);

        DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return getPath(source, target, dijkstraShortestPath);
    }

    private void addVertexes(Set<Station> stations, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdges(Lines lines, WeightedMultigraph<Station, ShortestPathEdge> graph) {
        for (Line line : lines.getLines()) {
            Sections sections = line.getSections();
            addEdge(graph, line, sections);
        }
    }

    private void addEdge(WeightedMultigraph<Station, ShortestPathEdge> graph, Line line, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), new ShortestPathEdge(line.getId(),
                    section.getDistance()));
        }
    }

    private Path getPath(Station source, Station target,
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        List<Station> vertexes = calculateShortestPath(source, target, dijkstraShortestPath);
        List<Long> lineIds = calculateShortestPathLines(source, target, dijkstraShortestPath);
        double distance = calculateShortestDistance(source, target, dijkstraShortestPath);
        return new Path(vertexes, lineIds, distance);
    }

    private List<Station> calculateShortestPath(Station source, Station target,
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private List<Long> calculateShortestPathLines(Station source, Station target,
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        List<ShortestPathEdge> edges = dijkstraShortestPath.getPath(source, target).getEdgeList();
        return edges.stream()
                .map(ShortestPathEdge::getLineId)
                .collect(Collectors.toList());
    }

    private double calculateShortestDistance(Station source, Station target,
            DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath) {
        return dijkstraShortestPath.getPathWeight(source, target);
    }

}
