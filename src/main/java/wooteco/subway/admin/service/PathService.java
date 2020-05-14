package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathSearchResponse;
import wooteco.subway.admin.dto.PathWeightedEdge;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PathService {
    private LineRepository lineRepository;

    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathSearchResponse searchPath(String source, String target) {
        WeightedMultigraph<String, PathWeightedEdge> pathGraph = new WeightedMultigraph(PathWeightedEdge.class);

        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();

        Map<Long, Station> stationMap = new HashMap<>(); //key = stationId, value = Station
        for (Station station : stations) {
            stationMap.put(station.getId(), station);
        }

        stations.forEach(station -> pathGraph.addVertex(station.getName()));

        lines.forEach(line -> {
            line.getLineStations().stream()
                    .filter(lineStation -> !Objects.isNull(lineStation.getPreStationId()))
                    .forEach(lineStation -> {
                        String preStationName = stationMap.get(lineStation.getPreStationId()).getName();
                        String nextStationName = stationMap.get(lineStation.getStationId()).getName();
                        PathWeightedEdge edge = pathGraph.addEdge(preStationName, nextStationName);
                        edge.setDistance(lineStation.getDistance());
                        edge.setDuration(lineStation.getDuration());
                        pathGraph.setEdgeWeight(edge, lineStation.getDistance());
                    });
        });

        GraphPath path = new DijkstraShortestPath(pathGraph).getPath(source, target);
        List<String> shortestPath = path.getVertexList();

        return new PathSearchResponse(calculateDuration(path.getEdgeList()), (int) path.getWeight(), shortestPath);
    }

    private int calculateDuration(List<PathWeightedEdge> edges) {
        return edges.stream()
                .map(edge -> edge.getDuration())
                .reduce(0, (a,b) -> a+b);
    }
}
