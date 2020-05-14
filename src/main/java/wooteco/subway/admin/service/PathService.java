package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(String source, String target, String type) {

        Lines allLines = new Lines(lineRepository.findAll());

        List<LineStation> lineStations = allLines.getAllLineStation();

        List<Station> allStations = stationRepository.findAll();

        Station sourceStation = findStationByName(source, allStations);
        Station targetStation = findStationByName(target, allStations);

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = initGraph(lineStations);

        List<Long> shortestPathIds = computeShortestPath(graph, sourceStation, targetStation);

        List<Station> shortestPath = new ArrayList<>();
        int distance = 0;
        int duration = 0;
        Long preStationId = sourceStation.getId();

        for (Long stationId : shortestPathIds) {
            shortestPath.add(findStationById(stationId, allStations));
            LineStation lineStation = findLineStation(preStationId, stationId, lineStations);
            distance += lineStation.getDistance();
            duration += lineStation.getDuration();
            preStationId = stationId;
        }

        return new PathResponse(StationResponse.listOf(shortestPath), distance, duration);
    }

    private List<Long> computeShortestPath(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Station sourceStation, Station targetStation) {
        // 다익스트라 초기화
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        // 가중치 최소 경로 구하기
        return (List<Long>) dijkstraShortestPath.getPath(sourceStation.getId(), targetStation.getId()).getVertexList();
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> initGraph(List<LineStation> lineStations) {
        // 그래프 초기화
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        // 모든 라인 스테이션 아이디 가져오기
        Set<Long> allStationIds = getAllLineStationId(lineStations);

        // 모든 점 추가
        for (Long stationId : allStationIds) {
            graph.addVertex(stationId);
        }

        // 모든 간선 추가
        for (LineStation lineStation : lineStations) {
            if (lineStation.getPreStationId() != null) {
                graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()), lineStation.getDistance());
            }
        }
        return graph;
    }

    private Set<Long> getAllLineStationId(List<LineStation> lineStations) {
        return lineStations.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toSet());
    }

    public Station findStationById(Long id, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.is(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Station findStationByName(String name, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.is(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public LineStation findLineStation(Long preStationId, Long stationId, List<LineStation> lineStations) {
        for (LineStation lineStation : lineStations) {
            if (lineStation.is(preStationId, stationId)) {
                return lineStation;
            }
            if (lineStation.is(stationId, preStationId)) {
                return lineStation;
            }
        }
        return LineStation.empty();
    }
}
