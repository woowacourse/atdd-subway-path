package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.domain.PathFinder;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        final Station source = stationService.findStationById(sourceId);
        final Station target = stationService.findStationById(targetId);

        final PathFinder pathFinder = new PathFinder(lineService.findLines());
        final GraphPath shortest = pathFinder.shortest(source, target);

        return new PathResponse(shortest);
    }
}
