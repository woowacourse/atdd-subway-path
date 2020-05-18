package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;
import java.util.Map;

@Service
public class PathService {
    private final LineService lineService;

    public PathService(LineService lineService) {
        this.lineService = lineService;
    }

    public PathResponse retrieveShortestPath(PathRequest pathRequest) {
        Station source = lineService.findStationWithName(pathRequest.getSource());
        Station target = lineService.findStationWithName(pathRequest.getTarget());

        Stations stations = Stations.of(lineService.findAllStations());
        Map<Long, Station> stationCache = stations.convertMap();

        Lines lines = Lines.of(lineService.showLines());
        List<LineStation> edges = lines.getEdges();

        Graph graph = Graph.of(stationCache, edges, pathRequest.getPathType());
        ShortestPath path = ShortestPath.of(graph, source, target);
        List<Station> shortestPath = path.findShortestPath();
        int totalDistance = path.getTotalDistance();
        int totalDuration = path.getTotalDuration();

        return PathResponse.of(StationResponse.listOf(shortestPath), totalDuration, totalDistance);
    }
}
