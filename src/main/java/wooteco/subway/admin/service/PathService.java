package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.EdgeType;
import wooteco.subway.admin.domain.Graph;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

import static wooteco.subway.admin.domain.EdgeType.DISTANCE;
import static wooteco.subway.admin.domain.EdgeType.DURATION;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SearchPathResponse searchPath(String startStationName, String targetStationName, String type) {
        validateStationSame(startStationName, targetStationName);

        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        Station startStation = findStationByName(startStationName, stations);
        Station targetStation = findStationByName(targetStationName, stations);

        Graph graph = Graph.of(lines, stations, EdgeType.of(type));

        int durationSum = graph.getEdgeValueSum(startStation, targetStation, DURATION);
        int distanceSum = graph.getEdgeValueSum(startStation, targetStation, DISTANCE);
        List<String> stationNames = graph.getVertexName(startStation, targetStation);

        return new SearchPathResponse(durationSum, distanceSum, stationNames);
    }

    private void validateStationSame(String startStationName, String targetStationName) {
        if (startStationName.equals(targetStationName)) {
            throw new IllegalArgumentException("시작역과 도착역이 같습니다.");
        }
    }

    private Station findStationByName(String stationName, List<Station> stations) {
        return stations.stream()
                .filter(station -> stationName.equals(station.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }
}
