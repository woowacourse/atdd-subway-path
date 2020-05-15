package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;

/**
 *    GraphService 클래스입니다.
 *
 *    @author HyungJu An, KuenHwi Choi
 */
@Service
public class GraphService {
	public GraphPath<Station, Edge> findPath(List<Line> lines, List<Station> stations,
		Station source, Station target, PathType type) {
		WeightedMultigraph<Station, Edge> graph
			= new WeightedMultigraph(Edge.class);

		stations.forEach(graph::addVertex);

		lines.stream()
			.flatMap(it -> it.getStations().stream())
			.filter(it -> Objects.nonNull(it.getPreStationId()))
			.forEach(it -> {
				Edge edge = Edge.of(it);
				graph.addEdge(findStation(stations, it.getPreStationId()),
					findStation(stations, it.getStationId()), edge);
				graph.setEdgeWeight(edge, type.findWeightOf(it));
			});

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		return dijkstraShortestPath.getPath(source, target);
	}

	private Station findStation(List<Station> stations, Long id) {
		return stations.stream()
			.filter(station -> station.getId().equals(id))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다." + id));
	}
}