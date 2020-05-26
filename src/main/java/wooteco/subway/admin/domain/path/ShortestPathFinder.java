package wooteco.subway.admin.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.exceptions.UnconnectedStationsException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ShortestPathFinder {
	private final DijkstraShortestPath<Long, DefaultWeightedEdge> path;

	public ShortestPathFinder(List<PathWeightEdge> pathWeightEdges) {
		WeightedMultigraph<Long, DefaultWeightedEdge> graph
				= new WeightedMultigraph<>(DefaultWeightedEdge.class);

		createVertexes(pathWeightEdges).forEach(graph::addVertex);

		pathWeightEdges.forEach(pathWeightEdge -> {
			PathEdge pathEdge = pathWeightEdge.pathEdge;
			DefaultWeightedEdge defaultWeightedEdge = graph.addEdge(pathEdge.departureVertex,
			                                                        pathEdge.destinationVertex);
			graph.setEdgeWeight(defaultWeightedEdge, pathWeightEdge.edgeWeight);
		});

		this.path = new DijkstraShortestPath<>(graph);
	}

	private Set<Long> createVertexes(List<PathWeightEdge> pathWeightEdges) {
		return pathWeightEdges.stream()
				.map(pathWeightEdge -> pathWeightEdge.pathEdge)
				.flatMap(pathEdge -> Stream.of(pathEdge.departureVertex, pathEdge.destinationVertex))
				.collect(Collectors.toSet());
	}

	public PathResult findShortestPath(Long departure, Long destination) {
		try {
			GraphPath<Long, DefaultWeightedEdge> path = this.path.getPath(departure, destination);
			List<Long> vertexes = path.getVertexList();
			List<PathEdge> pathEdges = IntStream.range(1, vertexes.size())
					.mapToObj(idx -> new PathEdge(vertexes.get(idx - 1), vertexes.get(idx)))
					.collect(Collectors.toList());
			return new PathResult(vertexes, pathEdges);
		} catch (Exception e) {
			throw new UnconnectedStationsException();
		}
	}
}
