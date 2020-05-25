package wooteco.subway.admin.domain.path;

public class PathWeightEdge {
	final PathEdge pathEdge;
	final double edgeWeight;

	public PathWeightEdge(PathEdge pathEdge, double edgeWeight) {
		this.pathEdge = pathEdge;
		this.edgeWeight = edgeWeight;
	}
}
