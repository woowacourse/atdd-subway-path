package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Sections;
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

    public PathServiceResponse findShortestPath(PathServiceRequest pathServiceRequest) {
        Sections sections = sectionService.findAll();
        Long departureId = pathServiceRequest.getDepartureId();
        Long arrivalId = pathServiceRequest.getArrivalId();

//        Path path = Path.of(sections);
//        List<Long> shortestPathStationIds = path.getShortestPathStationIds(departureId, arrivalId);
//        int distance = path.getShortestPathDistance(departureId, arrivalId);
//        int fare = path.calculateFare(new FarePolicy(pathServiceRequest.getAge()));

        List<Long> shortestPathStationIds = sections.getShortestPathStationIds(departureId, arrivalId);
        List<Station> stations = shortestPathStationIds.stream()
                .map(stationService::findById)
                .collect(Collectors.toList());

        int distance = sections.getShortestPathDistance(departureId, arrivalId);
        int fare = FareCalculator.getInstance().calculate(distance);
        return new PathServiceResponse(stations, distance, fare);
    }
}
