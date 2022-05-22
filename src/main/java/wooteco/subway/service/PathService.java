package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Subway;
import wooteco.subway.domain.vo.Path;
import wooteco.subway.dto.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        List<Line> lines = lineService.findAllLines();
        Subway subway = Subway.of(lines);
        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);

        Path path = subway.findShortestPath(source, target);
        return PathResponse.of(path, subway.calculateFare(path.getDistance()));
    }
}
