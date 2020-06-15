package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.errors.PathException;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(ShortestPathResponse shortestPathResponse) {
        checkSameStationName(shortestPathResponse.getSource(), shortestPathResponse.getTarget());
        Station sourceStation = findStationByName(shortestPathResponse.getSource());
        Station targetStation = findStationByName(shortestPathResponse.getTarget());

        Lines lines = new Lines(lineService.findAll());
        List<Long> path = Graph.findPath(lines, sourceStation.getId(), targetStation.getId()
                , PathType.valueOf(shortestPathResponse.getPathType()));

        Stations stations = new Stations(stationService.findAllById(path));

        LineStations lineStations = new LineStations();
        lineStations.findLineStationsWithOutSourceLineStation(lines);

        return new PathResponse(StationResponse.listOf(stations.findStationPath(path)), lineStations, path);
    }

    private Station findStationByName(String source) {
        return stationService.findByName(source);
    }

    private void checkSameStationName(String source, String target) {
        if (Objects.equals(source, target)) {
            throw new PathException("출발역과 도착역은 같은 지하철역이 될 수 없습니다.");
        }
    }
}
