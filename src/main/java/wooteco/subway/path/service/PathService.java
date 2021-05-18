package wooteco.subway.path.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService,
        StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse shortestPath(Long sourceId, Long targetId) {
        PathFinder pathFinder = pathFinder();
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        Path shortestPath = pathFinder.shortestPath(source, target);

        return new PathResponse(StationResponse.listOf(shortestPath.getStations()),
            shortestPath.getDistance());
    }

    private PathFinder pathFinder() {
        List<Station> allStations = stationService.findStations();
        List<Line> allLines = lineService.findLines();
        return new PathFinder(allStations, allLines);
    }
}
