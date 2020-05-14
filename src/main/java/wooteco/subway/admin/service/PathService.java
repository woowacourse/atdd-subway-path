package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.ShortestPath;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public ShortestPath findShortestDistancePath(String sourceName, String targetName) {
		Station sourceStation = stationRepository.findByName(sourceName)
				.orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 없습니다."));
		Station targetStation = stationRepository.findByName(targetName)
				.orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 없습니다."));

		List<Line> lines = lineRepository.findAll();

		List<Long> lineStationIds = lines.stream()
				.flatMap(line -> line.getLineStationsId().stream())
				.collect(Collectors.toList());

		List<Station> stations = stationRepository.findAllById(lineStationIds);

		Map<Long, Station> stationsCache = stations.stream()
				.collect(Collectors.toMap(Station::getId, Function.identity()));

		List<LineStation> lineStations = lines.stream()
				.flatMap(line -> line.getStations().stream())
				.collect(Collectors.toList());


		// 모든 역 => veteices에 넣어야하니까
		// 모든 라인 스테이션 => 엣지에 넣어야하니까 엣지는 이제 거리와 시간만있다.

		WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph(Edge.class);

		// 모든 라인에서 스테이션들을 다 가져와서 그래프에 넣는다.
		Graphs.addAllVertices(graph, stations);

		// 모든 라인스테이션을 가져와서 첫번째 두번째를 넣고 세번째에 거리를 넣는다.
		for (LineStation lineStation : lineStations) {
			if (lineStation.getPreStationId() == null) {
				continue;
			}
			Station station = stationsCache.get(lineStation.getStationId());
			Station preStation = stationsCache.get(lineStation.getPreStationId());

			Edge edge = new Edge(lineStation.getDuration(), lineStation.getDistance(), "distance");

			graph.addEdge(preStation, station, edge);
			graph.setEdgeWeight(graph.getEdge(preStation, station), edge.getDistance());
		}

		GraphPath<Station, Edge> result = getDijkstraShortestPath(graph, sourceStation, targetStation);

		int totalDistance = (int) result.getEdgeList().stream()
				.mapToDouble(Edge::getDistance)
				.sum();

		int totalDuration = (int) result.getEdgeList().stream()
				.mapToDouble(Edge::getDuration)
				.sum();

		return new ShortestPath(result.getVertexList(), totalDistance, totalDuration);
	}

	private GraphPath<Station, Edge> getDijkstraShortestPath(WeightedMultigraph<Station, Edge> graph, Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}
}
