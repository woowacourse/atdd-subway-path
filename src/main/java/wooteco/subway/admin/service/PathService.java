package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.LineStationRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private LineStationRepository lineStationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, LineStationRepository lineStationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineStationRepository = lineStationRepository;
    }

    public SearchPathResponse searchPath(String startStationName, String targetStationName, String type) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = makeGraph(type);
        List<Station> shortestPath = findShortestPath(graph, startStationName, targetStationName);
        int durationSum = calculateValueSum(shortestPath, "duration");
        int distanceSum = calculateValueSum(shortestPath, "distance");
        List<String> stationNames = toStationNames(shortestPath);

        return new SearchPathResponse(durationSum, distanceSum, stationNames);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(String type) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            makeRelation(graph, line, type);
        }
        return graph;
    }

    private void makeRelation(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line, String type) {
        for (LineStation lineStation : line.getStations()) {
            if (lineStation.getPreStationId() == null) {
                continue;
            }
            Station station = findStationById(lineStation.getStationId());
            Station preStation = findStationById(lineStation.getPreStationId());
            graph.addVertex(station);
            graph.addVertex(preStation);
            graph.setEdgeWeight(graph.addEdge(preStation, station), edgeValue(type, lineStation));
        }
    }

    private Station findStationById(Long preStationId) {
        return stationRepository.findById(preStationId)
                .orElseThrow(() -> new IllegalArgumentException("이전역이 존재하지 않습니다."));
    }

    private List<Station> findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, String startStationName, String targetStationName) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        Station startStation = findStationByName(startStationName);
        Station targetStation = findStationByName(targetStationName);

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(startStation, targetStation);
        validatePath(path);
        List<Station> shortestPath = path.getVertexList();
        validateShortestPath(shortestPath);

        return shortestPath;
    }

    private Station findStationByName(String startStationName) {
        return stationRepository.findByName(startStationName)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }

    private void validatePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("두 역이 연결되어있지 않습니다.");
        }
    }

    private void validateShortestPath(List<Station> shortestPath) {
        if (shortestPath.size() == 1) {
            throw new IllegalArgumentException("시작역과 도착역이 같습니다.");
        }
    }

    private List<String> toStationNames(List<Station> shortestPath) {
        return shortestPath.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

    private int calculateValueSum(List<Station> shortestPath, String type) {
        int valueSum = 0;
        for (int i = 1; i < shortestPath.size(); i++) {
            Station preStation = shortestPath.get(i - 1);
            Station station = shortestPath.get(i);
            valueSum += edgeValue(type, preStation, station);
        }
        return valueSum;
    }

    private int edgeValue(String type, Station preStation, Station station) {
        LineStation lineStation = findLineStationByIds(preStation, station);
        if (type.equals("duration")) {
            return lineStation.getDuration();
        }
        if (type.equals("distance")) {
            return lineStation.getDistance();
        }
        return 0;
    }

    private int edgeValue(String type, LineStation lineStation) {
        if (type.equals("duration")) {
            return lineStation.getDuration();
        }
        if (type.equals("distance")) {
            return lineStation.getDistance();
        }
        return 0;
    }

    private LineStation findLineStationByIds(Station preStation, Station station) {
        return lineStationRepository.findById(preStation.getId(), station.getId())
                .orElseThrow(()->new IllegalArgumentException("연결되지 않았습니다."));
    }
}
