package wooteco.subway.admin.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.NullStationIdException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;

public class Path {
	private final WeightedMultigraph<Long, DefaultWeightedEdge> durationGraph = new WeightedMultigraph<>(
		DefaultWeightedEdge.class);
	private final WeightedMultigraph<Long, DefaultWeightedEdge> distanceGraph = new WeightedMultigraph<>(
		DefaultWeightedEdge.class);
	private DijkstraShortestPath<Long, DefaultWeightedEdge> shortestDistancePath;
	private DijkstraShortestPath<Long, DefaultWeightedEdge> shortestDurationPath;

	public Path(List<Line> lines) {
		init(lines);
	}

	public void init(List<Line> lines) {
		for (Line line : lines) {
			setGraphWithLineStation(line);
		}
		this.shortestDistancePath = new DijkstraShortestPath<>(distanceGraph);
		this.shortestDurationPath = new DijkstraShortestPath<>(durationGraph);
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
			this.distanceGraph.setEdgeWeight(distanceGraph.addEdge(station.getPreStationId(), station.getStationId()),
				station.getDistance());
			this.durationGraph.setEdgeWeight(durationGraph.addEdge(station.getPreStationId(), station.getStationId()),
				station.getDuration());
		}
	}

	private GraphPath<Long, DefaultWeightedEdge> getShortestPath(Long source, Long target, PathSearchType type) {
		GraphPath<Long, DefaultWeightedEdge> path = null;
		validateNotNull(source, target);
		validateDifferentSourceAndDestination(source, target);
		if (type == PathSearchType.DISTANCE) {
			path = shortestDistancePath.getPath(source, target);
		}
		if (type == PathSearchType.DURATION) {
			path = shortestDurationPath.getPath(source, target);
		}
		validateConnectedPath(path);
		return path;
	}

	public List<Long> findShortestPath(Long source, Long target, PathSearchType type) {
		return getShortestPath(source, target, type)
			.getVertexList();
	}

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
