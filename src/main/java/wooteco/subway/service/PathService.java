package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.dto.path.PathRequest;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.dto.station.StationResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;

    public PathService(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        List<Section> sections = sectionService.findAll();
        Stations stations = stationService.findAll();
        Path path = Path.of(pathRequest.getSource(), pathRequest.getTarget(), sections, stations);
        return getPathResponse(stations, path.getPath(), path.getDistance());
    }

    private PathResponse getPathResponse(Stations stations, List<Long> shortestPath, int distance) {
        List<StationResponse> stationsResponses = getStationsResponses(stations, shortestPath);
        Fare fare = new Fare(distance);
        return new PathResponse(stationsResponses, distance, fare.calculate());
    }

    private List<StationResponse> getStationsResponses(Stations stations, List<Long> shortestPath) {
        return shortestPath.stream()
                .map(stations::findStationById)
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
