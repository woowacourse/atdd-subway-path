package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;

import java.util.List;

public class ShortestPath {
	private GraphPath<Station, Edge> shortestPath;

	public ShortestPath() {
	}

	public ShortestPath(GraphPath<Station, Edge> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public List<Station> getShortestPath() {
		return shortestPath.getVertexList();
	}

	public int calculateShortestDuration() {
		return shortestPath.getEdgeList().stream()
				.mapToInt(Edge::getDuration)
				.sum();
	}

	public int calculateShortestDistance() {
		return shortestPath.getEdgeList().stream()
				.mapToInt(Edge::getDistance)
				.sum();
	}
}
