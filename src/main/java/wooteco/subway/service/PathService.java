package wooteco.subway.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.farepolicy.FarePolicy;
import wooteco.subway.service.dto.LineServiceResponse;
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

    public PathServiceResponse findShortestPath(PathServiceRequest pathServiceRequest) {
        Long departureId = pathServiceRequest.getDepartureId();
        Long arrivalId = pathServiceRequest.getArrivalId();
        int age = pathServiceRequest.getAge();

        List<Section> sections = sectionService.findAll().getSections();
        Path path = Path.of(sections, departureId, arrivalId);

        List<Station> stations = getStations(path);
        int distance = path.getShortestPathDistance();
        int fare = getFare(age, sections, distance);

        return new PathServiceResponse(stations, distance, fare);
    }

    private List<Station> getStations(Path path) {
        List<Long> shortestPathStationIds = path.getShortestPathStationIds();
        return shortestPathStationIds.stream()
                .map(stationService::findById)
                .collect(Collectors.toList());
    }

    private int getFare(int age, List<Section> sections, int distance) {
        List<Integer> extraFares = sections.stream()
                .map(Section::getLineId)
                .map(lineService::findById)
                .map(LineServiceResponse::getExtraFare)
                .collect(Collectors.toList());
        Integer extraFare = Collections.max(extraFares);
        FareCalculator fareCalculator = new FareCalculator(FarePolicy.of(age));
        return fareCalculator.calculate(distance, extraFare);
    }
}
