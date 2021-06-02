package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.application.ValidationFailureException;
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
        validateDifferentStations(departureStationId, arrivalStationId);

        Station departureStation = stationService.findExistentStationById(departureStationId);
        Station arrivalStation = stationService.findExistentStationById(arrivalStationId);

        return PathResponse.from(
            pathRouter.findByShortest(departureStation, arrivalStation)
        );
    }

    private void validateDifferentStations(Long departureStationId, Long arrivalStationId) {
        if (departureStationId.equals(arrivalStationId)) {
            throw new ValidationFailureException("출발역과 도착역이 같습니다.");
        }
    }
}
