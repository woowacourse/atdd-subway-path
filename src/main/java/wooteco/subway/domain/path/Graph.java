package wooteco.subway.domain.path;

import static wooteco.subway.exception.InvalidPathException.NOT_CONNECTED_PATH;

import java.util.List;
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
		this.graph = createGraph(lines, strategy);
	}

	private WeightedMultigraph<Station, StationWeightEdge> createGraph(List<Line> lines, WeightStrategy strategy) {
		WeightedMultigraph<Station, StationWeightEdge> graph = new WeightedMultigraph<>(StationWeightEdge.class);

		stations.forEach(graph::addVertex);

		List<LineStation> possibleEdges = createPossibleEdges(lines);
		addEdge(graph, possibleEdges, strategy);

		return graph;
	}

	private List<LineStation> createPossibleEdges(List<Line> lines) {
		return lines.stream()
			.flatMap(line -> line.getStations().stream())
			.filter(LineStation::isNotFirstStation)
			.collect(Collectors.toList());
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

	public Path createPath(Station source, Station target) {
		GraphPath<Station, StationWeightEdge> path
			= new DijkstraShortestPath<>(graph).getPath(source, target);

		if (Objects.isNull(path)) {
			throw new InvalidPathException(NOT_CONNECTED_PATH, source.getName(), target.getName());
		}

		return new Path(path);
	}
}
