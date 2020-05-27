package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import wooteco.subway.admin.exception.NotConnectEdgeException;
import wooteco.subway.admin.util.StationsUtil;

public class Path {
    private final Long source;
    private final Long target;
    private final PathType pathType;
    private GraphPath<Station, Edge> path;

    public Path(Long source, Long target, PathType pathType, Map<Long, Station> stations, List<Line> lines) {
        this.source = source;
        this.target = target;
        this.pathType = pathType;
        this.path = getGraphPath(stations, lines);
    }

    public GraphPath<Station, Edge> getGraphPath(Map<Long, Station> stations, List<Line> lines) {
        final Station startStation = StationsUtil.findStationWithValidation(stations, source);
        final Station endStation = StationsUtil.findStationWithValidation(stations, target);

        final DijkstraShortestPath<Station, Edge> dijkstraShortestPath =
            new DijkstraShortestPath<>(Graph.of(stations, lines, pathType));

        final GraphPath<Station, Edge> path = dijkstraShortestPath.getPath(startStation, endStation);

        if (Objects.isNull(path)) {
            throw new NotConnectEdgeException();
        }

        return path;
    }

    public List<Station> getShortestPath() {
        return path.getVertexList();
    }

    public int getDistanceByWeight() {
        final List<Edge> edges = path.getEdgeList();

        return edges.stream().mapToInt(Edge::getDistance).sum();
    }

    public int getDurationByWeight() {
        final List<Edge> edges = path.getEdgeList();

        return edges.stream().mapToInt(Edge::getDuration).sum();
    }
}