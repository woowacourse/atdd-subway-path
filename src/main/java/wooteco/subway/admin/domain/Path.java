package wooteco.subway.admin.domain;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.NoExistPathException;

public class Path {
    private final WeightedMultigraph<Long, PathEdge> graph;

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
            List<LineStation> lineStations = line.getSortedLineStations();
            List<LineStation> edgeStations = filterValidEdgeStations(lineStations);
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
            throw new NoExistPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private void validateSourceTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new InvalidParameterException("출발역과 도착역은 같을 수 없습니다.");
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
