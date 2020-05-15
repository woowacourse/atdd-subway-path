package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public PathResponse showPaths(String source, String target, CriteriaType criteria) {
        validateSameStations(source, target);
        List<Line> lines = lineRepository.findAll();
        Station from = stationRepository.findByName(source)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Station to = stationRepository.findByName(target)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        List<Long> path = graphService.findPath(lines, from.getId(), to.getId(), criteria);
        List<Station> stations = stationRepository.findAllById(path);
        List<StationResponse> stationResponses = StationResponse.listOf(stations);

        List<LineStation> lineStations = lineStationsWithCriteria(lines, path);
        int totalDistance = sum(lineStations, CriteriaType.DISTANCE);
        int totalDuration = sum(lineStations, CriteriaType.DURATION);
        List<StationResponse> sortedStationResponses = sort(path, stationResponses);

        return new PathResponse(sortedStationResponses, totalDistance, totalDuration);
    }

    private List<LineStation> lineStationsWithCriteria(List<Line> lines, List<Long> path) {
        return lines.stream()
            .flatMap(line -> line.getStations().stream())
            .filter(lineStation -> path.contains(lineStation.getStationId()))
            .filter(lineStation -> Objects.isNull(lineStation.getPreStationId()) || path.contains(
                lineStation.getPreStationId()))
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .collect(Collectors.toList());
    }

    private int sum(List<LineStation> lineStations, CriteriaType type) {
        return lineStations.stream()
            .mapToInt(type::get)
            .sum();
    }

    private void validateSameStations(String source, String target) {
        if (source.equalsIgnoreCase(target)) {
            throw new IllegalArgumentException("동일역으로는 조회할 수 없습니다.");
        }
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
