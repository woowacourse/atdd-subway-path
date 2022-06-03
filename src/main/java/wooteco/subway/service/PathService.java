package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.AgeBoundary;
import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathDijkstraAlgorithm;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.dto.path.PathRequest;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.dto.station.StationResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionService sectionService;

    public PathService(StationService stationService, LineService lineService, SectionService sectionService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        List<Section> sections = sectionService.findAll();
        Stations stations = stationService.findAll();
        PathDijkstraAlgorithm algorithm = PathDijkstraAlgorithm.of(sections, stations);
        Path path = algorithm.findPath(pathRequest.getSource(), pathRequest.getTarget());

        List<StationResponse> stationsResponses = getStationsResponses(stations, path.getStationIds());
        Fare fare = getFare(path, pathRequest.getAge());
        return new PathResponse(stationsResponses, path.getDistance(), fare.getFare());
    }

    private List<StationResponse> getStationsResponses(Stations stations, List<Long> shortestPath) {
        return shortestPath.stream()
                .map(stations::findStationById)
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    private Fare getFare(Path path, int age) {
        Lines lines = lineService.findAll();
        Fare maxExtraFareByDistance = lines.findMaxExtraFareByDistance(path.getUsedLineIds());
        return path.calculateFareByDistance(maxExtraFareByDistance)
                .discountByAge(AgeBoundary.from(age));
    }
}
