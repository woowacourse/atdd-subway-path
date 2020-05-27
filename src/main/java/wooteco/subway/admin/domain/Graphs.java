package wooteco.subway.admin.domain;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import wooteco.subway.admin.domain.vo.Graph;
import wooteco.subway.admin.domain.vo.Path;
import wooteco.subway.admin.domain.vo.PathType;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StationResponse;

@Component
public class Graphs {

    private ConcurrentMap<PathType, Graph> graphs = new ConcurrentHashMap<>();
    private ConcurrentMap<Long, Station> stationMatcher = new ConcurrentHashMap<>();

    public void initialize(List<Line> lines, List<Station> stations) {
        stationMatcher = stations
            .stream()
            .collect(Collectors.toConcurrentMap(Station::getId, station -> station));
        for (PathType pathType : PathType.values()) {
            graphs.put(pathType, Graph.of(lines, stationMatcher, pathType));
        }
    }

    public PathResponse getPath(Long source, Long target, PathType pathType) {
        Graph graph = graphs.get(pathType);
        Path path = graph.getPath(source, target);
        List<Long> stationIds = getStations(path);
        List<LineStationEdge> edges = path.getEdgeList();
        List<StationResponse> responses = getStationResponses(stationIds);
        int distance = getDistance(edges);
        int duration = getDuration(edges);
        return PathResponse.of(responses, duration, distance);
    }

    private List<Long> getStations(Path path) {
        try {
            return path.getVertexList();
        } catch (IllegalArgumentException ie) {
            throw new IllegalArgumentException("등록되지 않은 역이 포함되어 있습니다.");
        } catch (NullPointerException ne) {
            throw new IllegalArgumentException("두 역은 연결되지 않아 갈 수 없습니다.");
        }
    }

    private int getDuration(List<LineStationEdge> edges) {
        return edges.stream()
            .mapToInt(LineStationEdge::getDuration)
            .sum();
    }

    private int getDistance(List<LineStationEdge> edges) {
        return edges.stream()
            .mapToInt(LineStationEdge::getDistance)
            .sum();
    }

    private List<StationResponse> getStationResponses(List<Long> stationIds) {
        return stationIds.stream()
            .map(stationMatcher::get)
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

}
