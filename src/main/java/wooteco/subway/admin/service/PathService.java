package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
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

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(List<Long> pathFormedId) {
        List<Station> stations = stationRepository.findAllById(pathFormedId);
        List<StationResponse> pathFormedStationResponse = StationResponse.listOf(stations);
        List<StationResponse> sortedStationResponses = sort(pathFormedId, pathFormedStationResponse);

        List<LineStation> lineStations = lineRepository.findAll().stream()
                .flatMap(line -> line.getStations().stream())
                .filter(lineStation -> isLineStationOnPath(pathFormedId, lineStation))
                .collect(Collectors.toList());

        int totalDistance = 0;
        int totalDuration = 0;
        for (LineStation lineStation : lineStations) {
            totalDistance += lineStation.getDistance();
            totalDuration += lineStation.getDuration();
        }

        return new PathResponse(sortedStationResponses, totalDistance, totalDuration);
    }

    private boolean isLineStationOnPath(List<Long> pathFormedId, LineStation lineStation) {
        return pathFormedId.contains(lineStation.getStationId())
                && (Objects.isNull(lineStation.getPreStationId()) || pathFormedId.contains(lineStation.getPreStationId()));
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
