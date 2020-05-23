package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.exceptions.NotExistLineStationException;
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.exceptions.UnconnectedStationsException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShortestPathFinder {
	private final Set<Station> allStations;
	private final Set<LineStation> lineStations;

	public ShortestPathFinder(List<Station> allStations, List<LineStation> allLineStations) {
		this.allStations = new HashSet<>(allStations);
		this.lineStations = allLineStations.stream()
				.filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
				.collect(Collectors.toSet());
	}

	public List<String> findShortestPathStationsNames(final Long sourceStationId, final Long targetStationId,
	                                                  final DijkstraEdgeWeightType edgeWeightType) {
		return findShortestPathStations(sourceStationId, targetStationId, edgeWeightType).stream()
				.map(this::findStationName)
				.collect(Collectors.toList());
	}

	public List<LineStation> findShortestPathLineStations(final Long sourceStationId, final Long targetStationId,
	                                                      final DijkstraEdgeWeightType edgeWeightType) {
		List<Long> shortestPathStations = findShortestPathStations(sourceStationId, targetStationId, edgeWeightType);
		return IntStream.range(0, shortestPathStations.size() - 1)
				.mapToObj(index -> findLineStation(shortestPathStations.get(index),
				                                   shortestPathStations.get(index + 1)))
				.collect(Collectors.toList());
	}

	private LineStation findLineStation(final Long preStationId, final Long stationId) {
		return lineStations.stream()
				.filter(it -> it.isSameLineStation(preStationId, stationId))
				.findFirst()
				.orElseThrow(() -> new NotExistLineStationException(preStationId, stationId));
	}

	private List<Long> findShortestPathStations(final Long sourceStationId, final Long targetStationId,
	                                            final DijkstraEdgeWeightType edgeWeightType) {
		DijkstraShortestPath dijkstraPath = findDijkstraShortestPath(edgeWeightType);
		try {
			return dijkstraPath.getPath(sourceStationId, targetStationId).getVertexList();
		} catch (NullPointerException e) {
			throw new UnconnectedStationsException();
		}
	}

	private DijkstraShortestPath findDijkstraShortestPath(final DijkstraEdgeWeightType edgeWeightType) {
		WeightedMultigraph<Long, DefaultWeightedEdge> graph
				= new WeightedMultigraph(DefaultWeightedEdge.class);

		allStations.forEach(station -> graph.addVertex(station.getId()));
		lineStations.forEach(lineStation -> graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(),
		                                                                      lineStation.getStationId()),
		                                                        edgeWeightType.getWeight(lineStation)));

		return new DijkstraShortestPath(graph);
	}

	private String findStationName(final Long stationId) {
		return allStations.stream()
				.filter(station -> station.hasEqualId(stationId))
				.findFirst()
				.map(Station::getName)
				.orElseThrow(() -> new NotExistStationException(stationId));
	}
}
