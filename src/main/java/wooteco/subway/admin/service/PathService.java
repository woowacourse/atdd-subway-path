package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.path.PathRequestWithId;
import wooteco.subway.admin.dto.path.PathResponse;
import wooteco.subway.admin.dto.station.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final GraphService graphService;

    public PathService(StationRepository stationRepository,
                       LineRepository lineRepository, GraphService graphService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.graphService = graphService;
    }

    public PathResponse findPath(PathRequestWithId pathRequestWithId) {
        List<Line> lines = lineRepository.findAll();
        List<Long> pathFormedId = graphService.findPath(lines, pathRequestWithId);

        List<Station> stations = stationRepository.findAllById(pathFormedId);
        List<StationResponse> pathFormedStationResponse = StationResponse.listOf(stations);

        List<LineStation> lineStations = lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(lineStation -> pathFormedId.contains(lineStation.getStationId()))
                .filter(lineStation -> Objects.isNull(lineStation.getPreStationId()) || pathFormedId.contains(lineStation.getPreStationId()))
                .collect(Collectors.toList());

        int totalDistance = lineStations.stream().mapToInt(LineStation::getDistance).sum();
        int totalDuration = lineStations.stream().mapToInt(LineStation::getDuration).sum();

        List<StationResponse> sortedStationResponses = sort(pathFormedId, pathFormedStationResponse);

        return new PathResponse(sortedStationResponses, totalDistance, totalDuration);
    }

    private List<StationResponse> sort(List<Long> path, List<StationResponse> stationResponses) {
        List<StationResponse> result = new ArrayList<>();

        for (Long stationId : path) {
            StationResponse response = stationResponses.stream()
                    .filter(stationResponse -> stationResponse.getId().equals(stationId))
                    .findAny().orElseThrow(IllegalArgumentException::new);
            result.add(response);
        }
        return result;
    }
}
