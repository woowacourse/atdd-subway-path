package wooteco.subway.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionService sectionService;
    private final PathFinder pathFinder;

    public PathService(StationService stationService, SectionService sectionService, LineService lineService,
                       PathFinder pathFinder) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target, int age) {
        Stations stations = stationService.findAll();
        validateStation(stations, source, target);

        Path path = pathFinder.find(stations.getStationIds(), sectionService.findAll(), source, target);
        List<StationResponse> stationResponses = getStationsResponses(stations, path.getStationIds());
        int fare = path.calculateFare(age, getExtraCharge(path.getLineIds()));

        return new PathResponse(stationResponses, path.getDistance(), fare);
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

    private List<StationResponse> getStationsResponses(Stations stations, List<Long> shortestPath) {
        return shortestPath.stream()
                .map(stations::findStationById)
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    private int getExtraCharge(Set<Long> lineIds) {
        List<Line> lines = lineService.findByIds(lineIds);
        return lines.stream()
                .distinct()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
