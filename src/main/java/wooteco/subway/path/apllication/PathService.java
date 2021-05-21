package wooteco.subway.path.apllication;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;


    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long source, Long target) {
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        return createPathResponse(subwayMap, source, target);
    }

    private PathResponse createPathResponse(SubwayMap subwayMap, Long source, Long target) {
        int shortestPath = subwayMap.getShortestDistance(source, target);
        List<Long> stationIdsOnPath = subwayMap.getShortestPathIds(source, target);
        List<Station> stationsOnPath = stationIdsOnPath.stream()
                .map(stationService::findStationById)
                .collect(Collectors.toList());

        return PathResponse.from(stationsOnPath, shortestPath);
    }
}
