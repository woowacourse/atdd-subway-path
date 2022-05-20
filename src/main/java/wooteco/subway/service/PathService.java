package wooteco.subway.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;

    public PathService(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public PathServiceResponse findShortestPath(PathServiceRequest pathServiceRequest,
                                                Function<List<Section>, Path> pathStrategy) {
        Path path = pathStrategy.apply(sectionService.findAll().getSections());
        Long departureId = pathServiceRequest.getDepartureId();
        Long arrivalId = pathServiceRequest.getArrivalId();
        List<Long> shortestPathStationIds = path.getShortestPathStationIds(departureId, arrivalId);
        List<Station> stations = shortestPathStationIds.stream()
                .map(stationService::findById)
                .collect(Collectors.toList());

        int distance = path.getShortestPathDistance(departureId, arrivalId);
        Fare fare = new Fare(distance);
        return new PathServiceResponse(stations, distance, fare.value());
    }
}
