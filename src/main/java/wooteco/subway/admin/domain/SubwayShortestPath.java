package wooteco.subway.admin.domain;

import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

public class SubwayShortestPath {

	private final ShortestPathAlgorithm<Station, SubwayWeightedEdge> shortestPathAlgorithm;

	public SubwayShortestPath(
		ShortestPathAlgorithm<Station, SubwayWeightedEdge> shortestPathAlgorithm) {
		this.shortestPathAlgorithm = shortestPathAlgorithm;
	}

	public List<Station> getPathStations(Station source, Station target) {
		return shortestPathAlgorithm.getPath(source, target).getVertexList();
	}

	public int getWeight(Station source, Station target) {
		return (int) shortestPathAlgorithm.getPathWeight(source, target);
	}

	public int getSubWeight(Station source, Station target) {
		return (int) shortestPathAlgorithm.getPath(source, target)
			.getEdgeList()
			.stream()
			.mapToDouble(SubwayWeightedEdge::getSubWeight)
			.sum();
	}

}
