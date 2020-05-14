package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.SubwayEdge;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.domain.type.weightstrategy.WeightStrategy;
import wooteco.subway.admin.dto.PathResponse;
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

	public static List<LineStation> makeLineStations(List<Line> lines) {
		List<LineStation> lineStations = new ArrayList<>();

		for (Line line : lines) {
			for (LineStation lineStation : line.getLineStations()) {
				if (lineStation.isStart()) {
					continue;
				}
				lineStations.add(lineStation);
			}
		}
		return lineStations;
	}

	public PathResponse findPath(String sourceName, String targetName, String type) {
		WeightType weightType = WeightType.of(type);

		List<Line> allLines = lineRepository.findAll();
		Map<Long, Station> allStationsById = stationRepository.findAll().stream()
			.collect(Collectors.toMap(Station::getId, station -> station));

		Station source = findStationByName(sourceName, allStationsById);
		Station target = findStationByName(targetName, allStationsById);

		List<LineStation> allLineStations = makeLineStations(allLines);
		Graph<Long, SubwayEdge> graph = createMultiGraph(
			allLineStations, allStationsById, weightType.getWeightStrategy());

		GraphPath dijkstraShortestPath = new DijkstraShortestPath(graph).getPath(source.getId(), target.getId());

		List<Long> shortestPathIds = dijkstraShortestPath.getVertexList();
		List<SubwayEdge> edgeList = dijkstraShortestPath.getEdgeList();

		int totalDuration = calculateTotalDuration(edgeList);
		int totalDistance = calculateTotalDistance(edgeList);
		List<Station> shortestPath = makeStationsById(allStationsById, shortestPathIds);

		return PathResponse.of(totalDistance, totalDuration, shortestPath);
	}

	private int calculateTotalDistance(List<SubwayEdge> edgeList) {
		return edgeList.stream()
			.mapToInt(SubwayEdge::getDistance)
			.sum();
	}

	private int calculateTotalDuration(List<SubwayEdge> edgeList) {
		return edgeList.stream()
			.mapToInt(SubwayEdge::getDuration)
			.sum();
	}

	private List<Station> makeStationsById(Map<Long, Station> allStationsById, List<Long> shortestPathIds) {
		List<Station> shortestPath = new ArrayList<>();

		for (Long stationId : shortestPathIds) {
			Station stationInPath = allStationsById.get(stationId);
			shortestPath.add(stationInPath);
		}
		return shortestPath;
	}

	private Graph<Long, SubwayEdge> createMultiGraph(List<LineStation> allLineStations,
		Map<Long, Station> allStationsById, WeightStrategy weightStrategy) {
		WeightedMultigraph<Long, SubwayEdge> graph = new WeightedMultigraph<>(SubwayEdge.class);

		for (Station station : allStationsById.values()) {
			graph.addVertex(station.getId());
		}

		for (LineStation lineStation : allLineStations) {
			SubwayEdge subwayEdge = new SubwayEdge(lineStation, weightStrategy);
			graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), subwayEdge);
		}
		return graph;
	}

	private Station findStationByName(String targetName, Map<Long, Station> stations) {
		return stations.values().stream().filter(station -> station.isSameName(targetName))
			.findAny().orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다."));
	}
}
