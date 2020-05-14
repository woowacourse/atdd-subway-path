package wooteco.subway.admin.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.NullStationIdException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;

public class Lines {
	private final List<Line> lines;
	private DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;
	private final WeightedMultigraph<Long, DefaultWeightedEdge> durationGraph = new WeightedMultigraph<>(
		DefaultWeightedEdge.class);

	public Lines(List<Line> lines) {
		this.lines = lines;
		init();
	}

	public void init() {
		WeightedMultigraph<Long, DefaultWeightedEdge> distanceGraph = new WeightedMultigraph<>(
			DefaultWeightedEdge.class);
		for (Line line : lines) {
			setGraphWithLineStation(distanceGraph, line);
		}
		this.dijkstraShortestPath = new DijkstraShortestPath<>(distanceGraph);
	}

	private void setGraphWithLineStation(WeightedMultigraph<Long, DefaultWeightedEdge> distanceGraph, Line line) {
		for (LineStation station : line.getStations()) {
			distanceGraph.addVertex(station.getStationId());
			this.durationGraph.addVertex(station.getStationId());
			setEdgeAndWeight(distanceGraph, station);
		}
	}

	private void setEdgeAndWeight(WeightedMultigraph<Long, DefaultWeightedEdge> distanceGraph, LineStation station) {
		if (station.getPreStationId() != null) {
			distanceGraph.setEdgeWeight(distanceGraph.addEdge(station.getPreStationId(), station.getStationId()),
				station.getDistance());
			this.durationGraph.setEdgeWeight(durationGraph.addEdge(station.getPreStationId(), station.getStationId()),
				station.getDuration());
		}
	}

	public GraphPath<Long, DefaultWeightedEdge> getPath(Long source, Long target) {
		validateNotNull(source, target);
		validateDifferentSourceAndDestination(source, target);
		GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
		validateConnectedPath(path);
		return path;
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

	public List<Long> findShortestPath(Long source, Long target) {
		return getPath(source, target)
			.getVertexList();
	}

	public int calculateDistance(Long source, Long target) {
		return (int)getPath(source, target).getWeight();
	}

	public int calculateDuration(Long source, Long target) {
		List<Long> shortestPath = findShortestPath(source, target);
		int wholeDuration = 0;
		for (int i = 1; i < shortestPath.size(); i++) {
			wholeDuration += (int)durationGraph.getEdgeWeight(
				durationGraph.getEdge(shortestPath.get(i - 1), shortestPath.get(i)));
		}
		return wholeDuration;
	}
}
