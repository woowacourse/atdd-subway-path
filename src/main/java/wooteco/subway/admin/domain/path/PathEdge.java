package wooteco.subway.admin.domain.path;

public class PathEdge {
	final Long departureVertex;
	final Long destinationVertex;

	public PathEdge(Long departureVertex, Long destinationVertex) {
		this.departureVertex = departureVertex;
		this.destinationVertex = destinationVertex;
	}

	public Long getDepartureVertex() {
		return departureVertex;
	}

	public Long getDestinationVertex() {
		return destinationVertex;
	}
}
