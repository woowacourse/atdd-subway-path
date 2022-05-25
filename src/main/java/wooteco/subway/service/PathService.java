package wooteco.subway.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Age;
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
    private final LineService lineService;

    public PathService(StationService stationService, SectionService sectionService, LineService lineService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineService = lineService;
    }

    public PathServiceResponse findShortestPath(PathServiceRequest pathServiceRequest,
                                                Function<List<Section>, Path> pathStrategy) {
        Path path = pathStrategy.apply(sectionService.findAll().getSections());
        List<Long> shortestPathStationIds = findShortestPathStationIds(path, pathServiceRequest);
        List<Station> stations = findShortestPathStationIds(shortestPathStationIds);
        int distance =
                path.getShortestPathDistance(pathServiceRequest.getDepartureId(), pathServiceRequest.getArrivalId());
        int fare = calculateFare(path, distance, pathServiceRequest);
        return new PathServiceResponse(stations, distance, fare);
    }

    private List<Long> findShortestPathStationIds(Path path, PathServiceRequest pathServiceRequest) {
        long departureId = pathServiceRequest.getDepartureId();
        long arrivalId = pathServiceRequest.getArrivalId();
        return path.getShortestPathStationIds(departureId, arrivalId);
    }

    private List<Station> findShortestPathStationIds(List<Long> shortestPathStationIds) {
        return shortestPathStationIds.stream()
                .map(stationService::findById)
                .collect(Collectors.toList());
    }

    private int calculateFare(Path path, int distance, PathServiceRequest pathServiceRequest) {
        Age age = new Age(pathServiceRequest.getAge());
        int highestExtraFare =
                lineService.findHighestExtraFareByIds(path.getShortestPathLineIds(pathServiceRequest.getDepartureId(),
                        pathServiceRequest.getArrivalId()));
        Fare fare = new Fare(distance, highestExtraFare, age.getValue());
        return fare.value();
    }
}
