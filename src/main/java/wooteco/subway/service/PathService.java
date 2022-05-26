package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Age;
import wooteco.subway.domain.FarePolicy;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;
    private final LineService lineService;
    private final PathFinder pathFinder;
    private final FarePolicy farePolicy;

    public PathService(StationService stationService, SectionService sectionService,
                       LineService lineService, PathFinder pathFinder, FarePolicy farePolicy) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
        this.farePolicy = farePolicy;
    }

    public PathServiceResponse findShortestPath(PathServiceRequest pathServiceRequest) {
        List<Section> sections = sectionService.findAll()
                .getSections();
        Path path = pathFinder
                .getShortestPath(sections, pathServiceRequest.getDepartureId(), pathServiceRequest.getArrivalId());
        List<Station> stations = findShortestPathStations(path.getStationIds());
        int distance = path.getDistance();
        Age age = new Age(pathServiceRequest.getAge());
        int fare = calculateFare(path, distance, age.getValue());
        return new PathServiceResponse(stations, distance, fare);
    }

    private List<Station> findShortestPathStations(List<Long> shortestPathStationIds) {
        return shortestPathStationIds.stream()
                .map(stationService::findById)
                .collect(Collectors.toList());
    }

    private int calculateFare(Path path, int distance, int age) {
        int highestExtraFare = lineService.findHighestExtraFareByIds(path.getLineIds());
        return farePolicy.calculate(distance, highestExtraFare, age);
    }
}
