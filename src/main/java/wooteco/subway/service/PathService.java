package wooteco.subway.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionService sectionService;

    public PathService(StationService stationService, SectionService sectionService, LineService lineService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineService = lineService;
    }

    public PathResponse findPath(Long source, Long target, int age) {
        Stations stations = stationService.findAll();
        validateStation(stations, source, target);

        Path path = Path.of(sectionService.findAll(), stations.getStationIds());
        List<Long> shortestPath = path.findPath(source, target);
        Set<Long> lineIds = path.getLineIds(source, target);
        int distance = path.findDistance(source, target);
        return getPathResponse(stations, shortestPath, lineIds, distance, age);
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

    private PathResponse getPathResponse(Stations stations, List<Long> shortestPath, Set<Long> lineIds, int distance,
                                         int age) {
        List<StationResponse> stationsResponses = getStationsResponses(stations, shortestPath);
        List<Line> lines = lineService.findByIds(lineIds);
        int extraCharge = lines.stream().distinct().mapToInt(Line::getExtraFare).max()
                .orElse(0);

        Fare fare = new Fare(distance, extraCharge, age);
        return new PathResponse(stationsResponses, distance, fare.calculate());
    }

    private List<StationResponse> getStationsResponses(Stations stations, List<Long> shortestPath) {
        return shortestPath.stream()
                .map(stations::findStationById)
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }
}
