package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Stations;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;

    public PathService(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public PathResponse findPath(Long source, Long target) {
        Stations stations = stationService.findAll();
        validateStation(stations, source, target);

        Path path = Path.of(sectionService.findAll(), stations.getStationIds());
        List<Long> shortestPath = path.findPath(source, target);
        int distance = path.findDistance(source, target);
        return getPathResponse(stations, shortestPath, distance);
    }

    private void validateStation(Stations stations, Long source, Long target) {
        validateStationExist(stations, source);
        validateStationExist(stations, target);
        validateStationSame(source, target);
    }

    private void validateStationExist(Stations stations, Long stationId) {
        if (!stations.contains(stationId)) {
            throw new DataNotExistException("존재하지 않는 역입니다.");
        }
    }

    private void validateStationSame(Long source, Long target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같을 수 없습니다.");
        }
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
