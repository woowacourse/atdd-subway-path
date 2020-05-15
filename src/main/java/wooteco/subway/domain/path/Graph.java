package wooteco.subway.domain.path;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.InvalidPathException;

public class Graph {
	private final WeightedMultigraph<Station, StationWeightEdge> graph;
	private final List<Station> stations;

	public Graph(List<Line> lines, List<Station> stations, WeightStrategy strategy) {
		this.stations = stations;
		this.graph = createGraph(lines, stations, strategy);
	}

	private WeightedMultigraph<Station, StationWeightEdge> createGraph(List<Line> lines,
		List<Station> stations, WeightStrategy strategy) {
		WeightedMultigraph<Station, StationWeightEdge> graph
			= new WeightedMultigraph<>(StationWeightEdge.class);

		stations.forEach(graph::addVertex);

		List<LineStation> possibleEdges = createPossibleEdges(lines);

		possibleEdges.forEach(
			lineStation -> {
				Station preStation = searchStationById(stations, lineStation.getPreStationId());
				Station station = searchStationById(stations, lineStation.getStationId());

				StationWeightEdge edge = new StationWeightEdge(lineStation);
				graph.addEdge(preStation, station, edge);
				graph.setEdgeWeight(edge, strategy.getWeight(edge));
			});
		return graph;
	}

	private List<LineStation> createPossibleEdges(List<Line> lines) {
		return lines.stream()
			.flatMap(line -> line.getStations().stream())
			.filter(LineStation::isNotFirstStation)
			.collect(Collectors.toList());
	}

	private Station searchStationById(List<Station> stations, Long id) {
		return stations.stream()
			.filter(station -> id.equals(station.getId()))
			.findFirst()
			.orElseThrow(AssertionError::new);
	}

	public Path createPath(String sourceName, String targetName) {
		Station source = findStationByName(stations, sourceName);
		Station target = findStationByName(stations, targetName);

		GraphPath<Station, StationWeightEdge> path
			= new DijkstraShortestPath<>(graph).getPath(source, target);

		if (Objects.isNull(path)) {
			throw new InvalidPathException(sourceName, targetName);
		}

		return new Path(path);
	}

	private Station findStationByName(List<Station> stations, String source) {
		return stations.stream()
			.filter(station -> source.equals(station.getName()))
			.findFirst()
			.orElseThrow(NoSuchElementException::new);
	}
}
