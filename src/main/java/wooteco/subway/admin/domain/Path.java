package wooteco.subway.admin.domain;

import static wooteco.subway.admin.domain.PathSearchType.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.NoSuchStationException;
import wooteco.subway.admin.exception.NullStationIdException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;

public class Path {
    private final WeightedMultigraph<Long, DefaultWeightedEdge> durationGraph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);
    private final WeightedMultigraph<Long, DefaultWeightedEdge> distanceGraph = new WeightedMultigraph<>(
        DefaultWeightedEdge.class);
    private GraphPath<Long, DefaultWeightedEdge> graphPath;

    public Path(List<Line> lines, Long sourceId, Long targetId, PathSearchType type) {
        init(lines);
        graphPath = getShortestPath(sourceId, targetId, type);
    }

    public void init(List<Line> lines) {
        lines.forEach(this::setGraphWithLineStation);
    }

    private void setGraphWithLineStation(Line line) {
        for (LineStation station : line.getStations()) {
            this.distanceGraph.addVertex(station.getStationId());
            this.durationGraph.addVertex(station.getStationId());
            setEdgeAndWeight(station);
        }
    }

    private void setEdgeAndWeight(LineStation station) {
        if (station.getPreStationId() != null) {
            this.distanceGraph.setEdgeWeight(
                distanceGraph.addEdge(station.getPreStationId(), station.getStationId()),
                station.getDistance());
            this.durationGraph.setEdgeWeight(
                durationGraph.addEdge(station.getPreStationId(), station.getStationId()),
                station.getDuration());
        }
    }

    private GraphPath<Long, DefaultWeightedEdge> getShortestPath(Long source, Long target,
        PathSearchType type) {
        validateNotNull(source, target);
        validateDifferentSourceAndDestination(source, target);

        GraphPath<Long, DefaultWeightedEdge> path;
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = null;

        if (type == PathSearchType.DISTANCE) {
            dijkstraShortestPath = new DijkstraShortestPath<>(this.distanceGraph);
        }
        if (type == DURATION) {
            dijkstraShortestPath = new DijkstraShortestPath<>(this.durationGraph);
        }
        try {
            path = dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new NoSuchStationException(e);
        }
        validateConnectedPath(path);
        return path;
    }

    public List<Long> findShortestPath() {
        return graphPath.getVertexList();
    }

    public int calculateDuration(List<Long> shortestPath, PathSearchType type) {
        if (type == DURATION) {
            return (int)graphPath.getWeight();
        }
        int wholeDuration = 0;
        for (int i = 1; i < shortestPath.size(); i++) {
            wholeDuration += (int)durationGraph.getEdgeWeight(
                durationGraph.getEdge(shortestPath.get(i - 1), shortestPath.get(i)));
        }
        return wholeDuration;
    }

    public int calculateDistance(List<Long> shortestPath, PathSearchType type) {
        if (type == DURATION) {
            int wholeDistance = 0;
            for (int i = 1; i < shortestPath.size(); i++) {
                wholeDistance += (int)distanceGraph.getEdgeWeight(
                    distanceGraph.getEdge(shortestPath.get(i - 1), shortestPath.get(i)));
            }
            return wholeDistance;
        }
        return (int)graphPath.getWeight();
    }

    private void validateConnectedPath(GraphPath<Long, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new DisconnectedPathException();
        }
    }

    private void validateDifferentSourceAndDestination(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameSourceAndDestinationException();
        }
    }

    private void validateNotNull(Long source, Long target) {
        if (source == null || target == null) {
            throw new NullStationIdException();
        }
    }
}
