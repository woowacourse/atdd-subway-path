package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.ShortestPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        List<Line> lines = lineService.findLines();
        ShortestPath shortestPath = new ShortestPath(lines);
        List<Long> stationIds = shortestPath.getPath(source, target);
        List<Station> path = stationIds.stream()
                .map(stationService::findStationById)
                .collect(Collectors.toList());
        int distance = shortestPath.distance(source, target);

        return new PathResponse(StationResponse.listOf(path), distance);
    }
}
