package wooteco.subway.domain.path;

import static wooteco.subway.exception.InvalidPathException.NOT_CONNECTED_PATH;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.InvalidPathException;

public class Graph {
	private WeightedMultigraph<Station, StationWeightEdge> graph;
	private List<Station> stations;

	public Graph(List<Line> lines, List<Station> stations, WeightStrategy strategy) {
		this.stations = stations;
		create(lines, stations, strategy);
	}

	public void create(List<Line> lines, List<Station> stations, WeightStrategy pathType) {
		this.stations = stations;
		graph = new WeightedMultigraph<>(StationWeightEdge.class);

		stations.forEach(graph::addVertex);

		List<LineStation> possibleEdges = createPossibleEdges(lines);
		addEdge(graph, possibleEdges, pathType);
	}

	private List<LineStation> createPossibleEdges(List<Line> lines) {
		List<LineStation> possibleEdges = new ArrayList<>();
		lines.stream()
			.map(Line::getStationsExcludeFirst)
			.forEach(possibleEdges::addAll);
		return possibleEdges;
	}

	private void addEdge(WeightedMultigraph<Station, StationWeightEdge> graph, List<LineStation> edges,
		WeightStrategy strategy) {

		edges.forEach(
			lineStation -> {
				Station preStation = searchStationById(lineStation.getPreStationId());
				Station station = searchStationById(lineStation.getStationId());

				StationWeightEdge edge = new StationWeightEdge(lineStation);
				graph.addEdge(preStation, station, edge);
				graph.setEdgeWeight(edge, strategy.getWeight(edge));
			});
	}

	private Station searchStationById(Long id) {
		return stations.stream()
			.filter(station -> id.equals(station.getId()))
			.findFirst()
			.orElseThrow(AssertionError::new);
	}

	public Path createPath(String sourceName, String targetName) {
		Station source = findStation(sourceName);
		Station target = findStation(targetName);

		GraphPath<Station, StationWeightEdge> path
			= new DijkstraShortestPath<>(graph).getPath(source, target);

		if (Objects.isNull(path)) {
			throw new InvalidPathException(NOT_CONNECTED_PATH, sourceName, targetName);
		}

		return new Path(path);
	}

	private Station findStation(String target) {
		return stations.stream().filter(station -> Objects.equals(station.getName(), target))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
}
