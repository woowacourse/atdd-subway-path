package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathType;

/**
 *    GraphService 클래스입니다.
 *
 *    @author HyungJu An, KuenHwi Choi
 */
@Service
public class GraphService {
	public List<Edge> findPath(List<Line> lines, Long source, Long target, PathType type) {
		WeightedMultigraph<Long, Edge> graph
			= new WeightedMultigraph(Edge.class);

		lines.stream()
			.flatMap(it -> it.getLineStationsId().stream())
			.forEach(graph::addVertex);

		lines.stream()
			.flatMap(it -> it.getStations().stream())
			.filter(it -> Objects.nonNull(it.getPreStationId()))
			.forEach(it -> {
				Edge edge = Edge.of(it);
				graph.addEdge(it.getPreStationId(), it.getStationId(), edge);
				graph.setEdgeWeight(edge, type.findWeightOf(it));
			});

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		return dijkstraShortestPath.getPath(source, target).getEdgeList();
	}
}