package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.exceptions.UnconnectedStationsException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShortestPathFinder {
	private final List<Station> allStations;
	private final List<LineStation> allLineStations;

	public ShortestPathFinder(List<Station> allStations, List<LineStation> allLineStations) {
		this.allStations = allStations;
		this.allLineStations = allLineStations;
	}

	public List<Station> findShortestPathStations(final Station source, final Station target,
	                                              final SearchType searchType) {
		List<String> shortestPathStationIds = findShortestPathStationIds(source, target, searchType);

		if (shortestPathStationIds.contains("")) {
			throw new UnconnectedStationsException();
		}

		return shortestPathStationIds.stream()
				.map(it -> findStation(allStations, Long.valueOf(it)))
				.collect(Collectors.toList());
	}

	private List<String> findShortestPathStationIds(Station source, Station target, SearchType searchType) {
		DijkstraShortestPath dijkstraShortestPath = findDijkstraPath(searchType);

		return dijkstraShortestPath.getPath(
				String.valueOf(source.getId()), String.valueOf(target.getId())).getVertexList();
	}

	private DijkstraShortestPath findDijkstraPath(final SearchType searchType) {
		WeightedMultigraph<String, DefaultWeightedEdge> graph
				= new WeightedMultigraph(DefaultWeightedEdge.class);

		graph.addVertex("");
		allStations.forEach(station -> graph.addVertex(String.valueOf(station.getId())));
		allLineStations.forEach(it -> {
			String preStationIdValue = createStringValueOf(it.getPreStationId());
			String stationIdValue = String.valueOf(it.getStationId());
			graph.setEdgeWeight(graph.addEdge(preStationIdValue, stationIdValue),
			                    searchType.isDistance() ? it.getDistance() : it.getDuration());
		});

		return new DijkstraShortestPath(graph);
	}

	private String createStringValueOf(final Long preStationId) {
		if (Objects.isNull(preStationId)) {
			return "";
		}
		return String.valueOf(preStationId);
	}

	private Station findStation(final List<Station> allStations, final Long stationId) {
		return allStations.stream()
				.filter(it -> Objects.equals(it.getId(), stationId))
				.findFirst()
				.orElseThrow(NoSuchElementException::new);
	}
}
