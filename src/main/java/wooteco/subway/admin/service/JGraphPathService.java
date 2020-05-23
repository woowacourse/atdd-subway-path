package wooteco.subway.admin.service;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathSearchType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.SubwayEdge;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Transactional
@Service
public class JGraphPathService implements PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public JGraphPathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public ShortestPathResponse findShortestPath(PathRequest request) {
		Stations stations = new Stations(stationRepository.findAll());
		Station source = stations.findByName(request.getSource());
		Station target = stations.findByName(request.getTarget());
		validateDifferentSourceAndDestination(source, target);
		WeightedMultigraph<Station, SubwayEdge> graph = initGraph(request, stations);
		return getShortestPathResponse(source, target, graph);
	}

	private void validateDifferentSourceAndDestination(Station source, Station target) {
		if (source.equals(target)) {
			throw new SameSourceAndDestinationException();
		}
	}

	private WeightedMultigraph<Station, SubwayEdge> initGraph(PathRequest request, Stations stations) {
		PathSearchType searchType = PathSearchType.of(request.getType());
		WeightedMultigraph<Station, SubwayEdge> graph = new WeightedMultigraph<>(SubwayEdge.class);
		initGraphVertex(graph, stations);
		initGraphEdges(searchType, stations, graph);
		return graph;
	}

	private void initGraphVertex(WeightedMultigraph<Station, SubwayEdge> graph, Stations stations) {
		for (Station station : stations) {
			graph.addVertex(station);
		}
	}

	private void initGraphEdges(PathSearchType type, Stations stations, WeightedMultigraph<Station, SubwayEdge> graph) {
		lineRepository.findAll().stream()
			.flatMap(line -> line.getStations().stream())
			.filter(LineStation::isNotFirstLineStation)
			.forEach(lineStation -> addOneEdgeAndSetWeight(type, stations, graph, lineStation));
	}

	private void addOneEdgeAndSetWeight(PathSearchType type, Stations stations, WeightedMultigraph<Station, SubwayEdge> graph,
		LineStation lineStation) {
		Station preStation = stations.findById(lineStation.getPreStationId());
		Station station = stations.findById(lineStation.getStationId());
		SubwayEdge edge = SubwayEdge.from(lineStation);
		graph.addEdge(preStation, station, edge);
		graph.setEdgeWeight(edge, type.getEdgeWeight(lineStation));
	}

	private ShortestPathResponse getShortestPathResponse(Station source, Station target,
		Graph<Station, SubwayEdge> graph) {
		DijkstraShortestPath<Station, SubwayEdge> dijkstraPath = new DijkstraShortestPath<>(graph);
		GraphPath<Station, SubwayEdge> calculatedPath = dijkstraPath.getPath(source, target);
		validateConnectedPath(calculatedPath);
		List<Station> paths = calculatedPath.getVertexList();
		int totalDuration = calculatedPath.getEdgeList().stream().mapToInt(SubwayEdge::getDuration).sum();
		int totalDistance = calculatedPath.getEdgeList().stream().mapToInt(SubwayEdge::getDistance).sum();
		return new ShortestPathResponse(StationResponse.listOf(paths), totalDistance, totalDuration);
	}

	private void validateConnectedPath(GraphPath<Station, SubwayEdge> path) {
		if (path == null) {
			throw new DisconnectedPathException();
		}
	}
}
