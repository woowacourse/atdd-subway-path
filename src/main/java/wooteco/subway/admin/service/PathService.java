package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.SourceTargetSameException;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(String sourceName, String targetName, String type) {
        validate(sourceName, targetName);

        Station source = stationService.findByName(sourceName);
        Station target = stationService.findByName(targetName);
        PathType pathType = PathType.of(type);

        List<Line> lines = lineService.findLines();
        List<Station> stations = stationService.findAll();

        SubwayGraph subwayGraph = new SubwayGraph(lines, stations, pathType::weight);
        SubWayPath subWayPath = subwayGraph.generatePath(source, target);

        return new PathResponse(StationResponse.listOf(subWayPath.stations()), subWayPath.distance(), subWayPath.duration());
    }

    private void validate(String sourceName, String targetName) {
        if (Objects.equals(targetName, sourceName)) {
            throw new SourceTargetSameException(sourceName);
        }
    }
}