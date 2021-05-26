package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final StationService stationService;
    private final PathRouter pathRouter;

    public PathService(StationService stationService, PathRouter pathRouter) {
        this.stationService = stationService;
        this.pathRouter = pathRouter;
    }

    public PathResponse findShortestPath(Long departureStationId, Long arrivalStationId) {
        Station departureStation = stationService.findStationById(departureStationId);
        Station arrivalStation = stationService.findStationById(arrivalStationId);

        return PathResponse.from(
            pathRouter.findByShortest(departureStation, arrivalStation)
        );
    }
}
