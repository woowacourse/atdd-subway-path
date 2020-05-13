package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Graph;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;

import java.util.List;
import java.util.Map;

@Service
public class PathService {
    private LineService lineService;

    public PathService(LineService lineService) {
        this.lineService = lineService;
    }

    public List<Station> retrieveShortestPath(String decodedSource, String decodedTarget) {
        Station source = lineService.findStationWithName(decodedSource);
        Station target = lineService.findStationWithName(decodedTarget);

        Stations stations = Stations.of(lineService.findAllStations());
        Map<Long, Station> stationCache = stations.convertMap();

        Lines lines = Lines.of(lineService.showLines());

        Graph graph = Graph.of(stationCache, lines.getEdges());

        return graph.findShortestPath(source, target);
    }
}
