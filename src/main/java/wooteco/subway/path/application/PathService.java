package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.domain.Subway;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private LineService lineService;
    private StationService stationService;
    private Subway subway;

    public PathService(LineService lineService, StationService stationService, Subway subway) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.subway = subway;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
       if (subway.isEmpty()) {
            subway.initializeSubway(lineService.findLines());
        }

        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        List<StationResponse> shortestPath = shortestPathStations(source, target);
        int distance = subway.findPathDistance(source, target);

        return new PathResponse(shortestPath, distance);
    }

    private List<StationResponse> shortestPathStations(Station source, Station target) {
        return subway.findPathRoute(source, target)
            .stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
    }
}
