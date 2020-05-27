package wooteco.subway.admin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.CustomEdge;
import wooteco.subway.admin.domain.EdgeWeightType;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.InaccessibleStationException;
import wooteco.subway.admin.exception.NonExistentDataException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse findPath(String departStationName, String arrivalStationName, String type) {
		if (departStationName.equals(arrivalStationName)) {
			throw new IllegalArgumentException("출발지와 도착지는 같을 수 없습니다.");
		}
		Station departStation = getStationByName(departStationName);
		Station arrivalStation = getStationByName(arrivalStationName);
		List<Station> stations = stationRepository.findAll();
		List<Line> lines = lineRepository.findAll();

		return Optional.of(getGraphPath(stations, lines, EdgeWeightType.of(type)))
			.map(graph -> graph.getPath(departStation.getId(), arrivalStation.getId()))
			.map(path -> PathResponse.of(mapToStationResponse(path.getVertexList()), sumDistance(path),
				sumDuration(path)))
			.orElseThrow(() -> new InaccessibleStationException(arrivalStation.getName()));
	}

	private Station getStationByName(String departStationName) {
		return stationRepository.findByName(departStationName)
			.orElseThrow(() -> new NonExistentDataException(departStationName));
	}

	private int sumDistance(GraphPath<Long, CustomEdge> path) {
		return path.getEdgeList()
			.stream()
			.mapToInt(CustomEdge::getDistance)
			.sum();
	}

	private int sumDuration(GraphPath<Long, CustomEdge> path) {
		return path.getEdgeList()
			.stream()
			.mapToInt(CustomEdge::getDuration)
			.sum();
	}

	private List<StationResponse> mapToStationResponse(List<Long> shortestPath) {
		return shortestPath.stream()
			.map(id -> stationRepository.findById(id).orElseThrow(NoSuchElementException::new))
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}

	private DijkstraShortestPath<Long, CustomEdge> getGraphPath(List<Station> stations,
		List<Line> lines, EdgeWeightType type) {
		WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);
		stations.forEach(station -> graph.addVertex(station.getId()));
		lines.stream()
			.flatMap(line -> line.getStations().stream())
			.forEach(lineStation -> setGraph(graph, lineStation, type));

		return new DijkstraShortestPath<>(graph);
	}

	private void setGraph(WeightedMultigraph<Long, CustomEdge> graph, LineStation lineStation, EdgeWeightType type) {
		Long preStationId = lineStation.getPreStationId();
		Long stationId = lineStation.getStationId();

		if (Objects.nonNull(preStationId)) {
			CustomEdge customEdge = new CustomEdge(lineStation, type);
			graph.addEdge(preStationId, stationId, customEdge);
			graph.setEdgeWeight(customEdge, customEdge.getWeight());
		}
	}
}
