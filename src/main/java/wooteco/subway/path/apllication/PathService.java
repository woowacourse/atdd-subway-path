package wooteco.subway.path.apllication;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;

    public PathService(LineService lineService) {
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Station sourceStation = lineService.findStationById(source);
        Station targetStation = lineService.findStationById(target);
        return createPathResponse(subwayMap, sourceStation, targetStation);
    }

    private PathResponse createPathResponse(SubwayMap subwayMap, Station sourceStation, Station targetStation) {
        int shortestPath = subwayMap.getShortestPath(sourceStation, targetStation);
        List<Station> stationsOnPath = subwayMap.getStationsOnPath(sourceStation, targetStation);

        return new PathResponse(StationResponse.listOf(stationsOnPath), shortestPath);
    }
}
