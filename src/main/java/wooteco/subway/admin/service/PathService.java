package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
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

	public PathResponse findPath(String sourceName, String targetName) {
		WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		List<Line> lines = lineRepository.findAll();
		List<Station> stations = stationRepository.findAll();

		stations.forEach(System.out::println);

		Station source = stations.stream().filter(station -> station.isSameName(sourceName))
			.findAny().orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다."));
		Station target = stations.stream().filter(station -> station.isSameName(targetName))
			.findAny().orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다."));

		for (Station station : stations) {
			graph.addVertex(station.getId());
		}

		List<LineStation> lineStations = new ArrayList<>();

		for (Line line : lines) {
			for (LineStation lineStation : line.getLineStations()) {
				if (lineStation.isStart()) {
					continue;
				}
				graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()),
					lineStation.getDuration());

				lineStations.add(lineStation);
			}
		}
		GraphPath dijkstraShortestPath = new DijkstraShortestPath(graph).getPath(source.getId(), target.getId());
		List<Long> shortestPathId = dijkstraShortestPath.getVertexList();
		int totalDuration = (int)dijkstraShortestPath.getWeight();

		List<LineStation> lineStationInPaths = new ArrayList<>();
		for (Long stationId : shortestPathId.subList(1, shortestPathId.size())) {
			LineStation lineStationInPath = lineStations.stream()
				.filter(lineStation -> lineStation.isSameStationId(stationId))
				.findAny()
				.orElseThrow(() -> new IllegalStateException("아니 이럴수가1?"));
			lineStationInPaths.add(lineStationInPath);
		}
		int totalDistance = lineStationInPaths.stream()
			.mapToInt(LineStation::getDistance)
			.sum();

		List<Station> shortestPath = new ArrayList<>();
		for (Long stationId : shortestPathId) {
			Station stationInPath = stations.stream()
				.filter(station -> station.isSameId(stationId))
				.findAny()
				.orElseThrow(() -> new IllegalStateException("아니 이럴수가2?"));

			shortestPath.add(stationInPath);
		}

		return PathResponse.of(totalDistance, totalDuration, shortestPath);
	}
}
