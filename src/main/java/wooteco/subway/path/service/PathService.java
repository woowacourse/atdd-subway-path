package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public Station findStationById(long stationId) {
        return stationService.findStationById(stationId);
    }

    public Path getPath(Station source, Station target) {
        SubwayMap subwayMap = new SubwayMap();
        subwayMap.initPath(lineService.findLines());

        return subwayMap.getShortestPath(source, target);
    }
}
