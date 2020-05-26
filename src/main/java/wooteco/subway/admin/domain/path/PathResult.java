package wooteco.subway.admin.domain.path;

import java.util.List;

public class PathResult {
	private final List<Long> vertexes;
	private final List<PathEdge> edges;

	public PathResult(List<Long> vertexes, List<PathEdge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Long> getVertexes() {
		return vertexes;
	}

	public List<PathEdge> getEdges() {
		return edges;
	}
}
