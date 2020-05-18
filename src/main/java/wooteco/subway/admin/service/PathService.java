package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.GraphResultResponse;
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
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "잘못된 소스와 타켓을 요청하였습니다")
    public PathResponse showPaths(String source, String target, CriteriaType criteria) {
        validateSameStations(source, target);

        List<Line> lines = lineRepository.findAll();
        Station from = stationRepository.findByName(source)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        Station to = stationRepository.findByName(target)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        GraphResultResponse result = graphService.findPath(lines, from.getId(), to.getId(),
            criteria);
        List<Station> stations = stationRepository.findAllById(result.getStationIds());
        List<StationResponse> stationResponses = StationResponse.listOf(stations);

        List<StationResponse> sortedStationResponses = sort(result.getStationIds(), stationResponses);

        return new PathResponse(sortedStationResponses, result.getDistance(), result.getDuration());
    }

    private void validateSameStations(String source, String target) {
        if (source.equalsIgnoreCase(target)) {
            throw new IllegalArgumentException("동일역으로는 조회할 수 없습니다.");
        }
    }

    private List<StationResponse> sort(List<Long> shortestPath, List<StationResponse> stationResponses) {
        List<StationResponse> result = new ArrayList<>();

        for (Long stationId : shortestPath) {
            StationResponse response = findByStationIdFrom(stationResponses, stationId);
            result.add(response);
        }
        return result;
    }

    private StationResponse findByStationIdFrom(List<StationResponse> stationResponses, Long stationId) {
        return stationResponses.stream()
            .filter(stationResponse -> stationResponse.getId().equals(stationId))
            .findAny().orElseThrow(IllegalArgumentException::new);
    }
}
