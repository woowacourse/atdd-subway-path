package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
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
        List<Station> stations = stationService.findAll();
        List<Long> stationIds = getStationIds(stations);
        validateStation(source, target, stationIds);

        Path path = Path.of(sectionService.findAll(), stationIds);
        List<Long> shortestPath = path.findPath(source, target);
        int distance = path.findDistance(source, target);
        return getPathResponse(stations, shortestPath, distance);
    }

    private List<Long> getStationIds(List<Station> stations) {
        return stations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    private void validateStation(Long source, Long target, List<Long> stationIds) {
        validateStationExist(stationIds, source);
        validateStationExist(stationIds, target);
        validateStationSame(source, target);
    }

    private void validateStationExist(List<Long> stationIds, Long stationId) {
        if (!stationIds.contains(stationId)) {
            throw new DataNotExistException("존재하지 않는 역입니다.");
        }
    }

    private void validateStationSame(Long source, Long target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private PathResponse getPathResponse(List<Station> stations, List<Long> shortestPath, int distance) {
        List<StationResponse> stationsResponses = getStationsResponses(stations, shortestPath);
        Fare fare = new Fare(distance);
        return new PathResponse(stationsResponses, distance, fare.calculate());
    }

    private List<StationResponse> getStationsResponses(List<Station> stations, List<Long> shortestPath) {
        return shortestPath.stream()
                .map(id -> sectionService.getStationById(stations, id))
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
