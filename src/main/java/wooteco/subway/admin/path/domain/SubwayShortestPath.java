package wooteco.subway.admin.path.domain;

import java.util.List;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

import wooteco.subway.admin.station.domain.Station;

public class SubwayShortestPath {

	private final ShortestPathAlgorithm<Station, SubwayWeightedEdge> shortestPathAlgorithm;

	public SubwayShortestPath(
		ShortestPathAlgorithm<Station, SubwayWeightedEdge> shortestPathAlgorithm) {
		this.shortestPathAlgorithm = shortestPathAlgorithm;
	}

	public List<Station> getPathStations(Station source, Station target) {
		try {
			return shortestPathAlgorithm.getPath(source, target).getVertexList();
		} catch (NullPointerException e) {
			throw new InvalidSubwayPathException("지하철 경로를 조회할 수 없습니다.");
		}
	}

	public int getWeight(Station source, Station target) {
		double pathWeight = shortestPathAlgorithm.getPathWeight(source, target);
		if (pathWeight == Double.POSITIVE_INFINITY) {
			throw new InvalidSubwayPathException("지하철 경로를 조회할 수 없습니다.");
		}
		return (int)pathWeight;
	}

	public int getSubWeight(Station source, Station target) {
		try {
			return (int)shortestPathAlgorithm.getPath(source, target)
				.getEdgeList()
				.stream()
				.mapToDouble(SubwayWeightedEdge::getSubWeight)
				.sum();
		} catch (NullPointerException e) {
			throw new InvalidSubwayPathException("지하철 경로를 조회할 수 없습니다.");
		}
	}

}
