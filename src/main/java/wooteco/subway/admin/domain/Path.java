package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.DuplicateSourceTargetStationException;
import wooteco.subway.admin.exception.InvalidPathException;

public class Path {
    private WeightedMultigraph<Long, PathEdge> graph;

    public Path() {
        graph = new WeightedMultigraph<>(PathEdge.class);
    }

    public void addVertexes(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
    }

    public void setEdges(List<Line> lines, PathType pathType) {
        for (Line line : lines) {
            List<LineStation> stations = line.getLineStations();
            List<LineStation> edgeStations = filterValidEdgeStations(stations);
            addEdgesWithWeight(edgeStations, pathType);
        }
    }

    private List<LineStation> filterValidEdgeStations(List<LineStation> lineStations) {
        return lineStations.stream()
            .filter(LineStation::isNotFirstLineStation)
            .collect(Collectors.toList());
    }

    private void addEdgesWithWeight(List<LineStation> lineStations, PathType pathType) {
        for (LineStation lineStation : lineStations) {
            PathEdge pathEdge = PathEdge.of(lineStation, pathType);
            graph.addEdge(pathEdge.getPreStationId(), pathEdge.getStationId(), pathEdge);
            graph.setEdgeWeight(pathEdge, pathEdge.getWeight());
        }
    }

    public GraphPath<Long, PathEdge> searchShortestPath(Station source, Station target) {
        validateSourceTarget(source, target);

        GraphPath<Long, PathEdge> shortestPath = DijkstraShortestPath.findPathBetween(graph,
            source.getId(), target.getId());

        validatePath(shortestPath);
        return shortestPath;
    }

    private void validatePath(GraphPath<Long, PathEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new InvalidPathException();
        }
    }

    private void validateSourceTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new DuplicateSourceTargetStationException();
        }
    }

    public int calculateDistance(GraphPath<Long, PathEdge> path) {
        return path.getEdgeList()
            .stream()
            .mapToInt(PathEdge::getDistance)
            .sum();
    }

    public int calculateDuration(GraphPath<Long, PathEdge> path) {
        return path.getEdgeList()
            .stream()
            .mapToInt(PathEdge::getDuration)
            .sum();
    }
}
