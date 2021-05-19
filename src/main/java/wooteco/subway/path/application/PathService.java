package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.domain.SubwayMap;
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

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        SubwayMap subwayMap = new SubwayMap(lineService.findLines());

        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        List<StationResponse> shortestPath = subwayMap.findPathRoute(source, target)
            .stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
        int distance = subwayMap.findPathDistance(source, target);

        return new PathResponse(shortestPath, distance);
    }
}
