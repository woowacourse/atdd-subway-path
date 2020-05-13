package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Graph;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineService lineService;

    public PathService(LineService lineService) {
        this.lineService = lineService;
    }

    public List<Station> retrieveShortestPath(String decodedSource, String decodedTarget) {
        Station source = lineService.findStationWithName(decodedSource);
        Station target = lineService.findStationWithName(decodedTarget);

        List<Station> stations = lineService.findAllStations();
        Map<Long, Station> stationCache = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        List<Line> lines = lineService.showLines();
        List<LineStation> edges = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Graph graph = Graph.of(stationCache, edges);

        return graph.findShortestPath(source, target);
    }
}
