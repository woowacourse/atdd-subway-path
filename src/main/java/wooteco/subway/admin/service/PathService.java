package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.ShortestPath;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional()
	public ShortestPath findShortestDistancePath(String sourceName, String targetName) {
		Station sourceStation = stationRepository.findByName(sourceName)
				.orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 없습니다."));
		Station targetStation = stationRepository.findByName(targetName)
				.orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 없습니다."));

		Subway subway = new Subway(lineRepository.findAll());
		List<Long> lineStationIds = subway.fetchLineStationIds();
		List<LineStation> lineStations = subway.fetchLineStations();

		Stations stations = new Stations(stationRepository.findAllById(lineStationIds));
		List<Station> vertices = stations.getStations();

		/////////////////////////////////////////////////////////////////
		WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph(Edge.class);

		// 모든 라인에서 스테이션들을 다 가져와서 그래프에 넣는다.
		Graphs.addAllVertices(graph, vertices);

		// 모든 라인스테이션을 가져와서 첫번째 두번째를 넣고 세번째에 거리를 넣는다.
		for (LineStation lineStation : lineStations) {
			if (lineStation.getPreStationId() == null) {
				continue;
			}
			Station station = stations.findByKey(lineStation.getStationId());
			Station preStation = stations.findByKey(lineStation.getPreStationId());

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
