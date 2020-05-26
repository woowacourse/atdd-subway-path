package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.PathInfo;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.ShortestPath;
import wooteco.subway.admin.domain.Station;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private GraphService graphService;

    public PathService(LineService lineService, GraphService graphService) {
        this.lineService = lineService;
        this.graphService = graphService;
    }

    public PathInfo retrieveShortestPath(String sourceName, String targetName, PathType pathType) {
        Station source = lineService.findStationWithName(sourceName);
        Station target = lineService.findStationWithName(targetName);

        ShortestPath path = graphService.getShortestPath(source, target, pathType);
        List<Station> shortestPath = path.findShortestPath();
        int totalDistance = path.getTotalDistance();
        int totalDuration = path.getTotalDuration();

        return new PathInfo(shortestPath, totalDuration, totalDistance);
    }
}
