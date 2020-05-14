package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final GraphService graphService;

    public PathService(LineRepository lineRepository,
        StationRepository stationRepository, GraphService graphService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.graphService = graphService;
    }

    public PathResponse showPaths(String source, String target, CriteriaType criteria) {
        List<Line> lines = lineRepository.findAll();
        final Station from = stationRepository.findByName(source)
            .orElseThrow(IllegalArgumentException::new);
        final Station to = stationRepository.findByName(target)
            .orElseThrow(IllegalArgumentException::new);
        List<Long> path = graphService.findPath(lines, from.getId(), to.getId(), criteria);
        List<Station> stations = stationRepository.findAllById(path);

        List<StationResponse> stationResponses = StationResponse.listOf(stations);

        List<LineStation> lineStations = lines.stream()
            .flatMap(line -> line.getStations().stream())
            .filter(lineStation -> path.contains(lineStation.getStationId()))
            .filter(lineStation -> Objects.isNull(lineStation.getPreStationId()) || path.contains(
                lineStation.getPreStationId()))
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .collect(Collectors.toList());

        int totalDistance = lineStations.stream().mapToInt(LineStation::getDistance).sum();
        int totalDuration = lineStations.stream().mapToInt(LineStation::getDuration).sum();

        List<StationResponse> sortedStationResponses = sort(path, stationResponses);

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
