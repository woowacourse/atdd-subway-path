package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.routemap.application.RouteMapManager;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final RouteMapManager routeMapService;
    private final PathRouter pathRouter;

    public PathService(final StationService stationService, LineService lineService, RouteMapManager routeMapService,
        PathRouter pathRouter) {

        this.stationService = stationService;
        this.lineService = lineService;
        this.routeMapService = routeMapService;
        this.pathRouter = pathRouter;
    }

    public PathResponse findShortestPath(Long departureStationId, Long arrivalStationId) {
        Station departureStation = stationService.findStationById(departureStationId);
        Station arrivalStation = stationService.findStationById(arrivalStationId);
        routeMapService.updateMap(
            lineService.findLines()
        );
        return PathResponse.from(
            pathRouter.findByShortest(departureStation, arrivalStation)
        );
    }
}
