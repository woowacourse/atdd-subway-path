package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;
import java.util.Map;

@Service
public class PathService {
    private LineService lineService;
    private GraphService graphService;

    public PathService(LineService lineService, GraphService graphService) {
        this.lineService = lineService;
        this.graphService = graphService;
    }

    public PathResponse retrieveShortestPath(String sourceName, String targetName, PathType pathType) {
        Station source = lineService.findStationWithName(sourceName);
        Station target = lineService.findStationWithName(targetName);

        ShortestPath path = graphService.getShortestPath(source, target, pathType);
        List<Station> shortestPath = path.findShortestPath();
        int totalDistance = path.getTotalDistance();
        int totalDuration = path.getTotalDuration();

        return PathResponse.of(StationResponse.listOf(shortestPath), totalDuration, totalDistance);
    }
}
