package wooteco.subway.admin.domain;

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
    private final GraphPath<Long, DefaultWeightedEdge> graphPath = null;

    public Path(List<Line> lines) {
        init(lines);
    }

    /** 초기화 */

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

    /** 최단경로 그래프 */

    private GraphPath<Long, DefaultWeightedEdge> getShortestPath(Long source, Long target,
        PathSearchType type) {
        validateNotNull(source, target);
        validateDifferentSourceAndDestination(source, target);

        GraphPath<Long, DefaultWeightedEdge> path;
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = null;

        if (type == PathSearchType.DISTANCE) {
            dijkstraShortestPath = new DijkstraShortestPath<>(this.distanceGraph);
        }
        if (type == PathSearchType.DURATION) {
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

    /** 최단 경로 **/

    public List<Long> findShortestPath(Long source, Long target, PathSearchType type) {
        return getShortestPath(source, target, type).getVertexList();
    }

    /** 최단시간 거리와 시간 **/

    public int calculateShortestDuration(Long source, Long target) {
        return (int)getShortestPath(source, target, PathSearchType.DURATION).getWeight();
    }

    public int calculateDistanceForShortestDurationPath(Long source, Long target) {
        List<Long> shortestPath = findShortestPath(source, target, PathSearchType.DURATION);
        int wholeDistance = 0;
        for (int i = 1; i < shortestPath.size(); i++) {
            wholeDistance += (int)distanceGraph.getEdgeWeight(
                distanceGraph.getEdge(shortestPath.get(i - 1), shortestPath.get(i)));
        }
        return wholeDistance;
    }

    /** 최단거리 거리와 시간 **/

    public int calculateShortestDistance(Long source, Long target) {
        return (int)getShortestPath(source, target, PathSearchType.DISTANCE).getWeight();
    }

    public int calculateDurationForShortestDistancePath(Long source, Long target) {
        List<Long> shortestPath = findShortestPath(source, target, PathSearchType.DISTANCE);
        int wholeDuration = 0;
        for (int i = 1; i < shortestPath.size(); i++) {
            wholeDuration += (int)durationGraph.getEdgeWeight(
                durationGraph.getEdge(shortestPath.get(i - 1), shortestPath.get(i)));
        }
        return wholeDuration;
    }

    /** validation */

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
