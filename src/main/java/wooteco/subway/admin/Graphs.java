package wooteco.subway.admin;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import wooteco.subway.admin.domain.Graph;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStationEdge;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StationResponse;

@Component
public class Graphs {

    private EnumMap<PathType, Graph> graphs = new EnumMap<>(PathType.class);
    private Map<Long, Station> stationMatcher = new HashMap<>();

    public void initialize(List<Line> lines, List<Station> stations) {
        stationMatcher = stations
            .stream()
            .collect(Collectors.toMap(Station::getId, station -> station));
        for (PathType pathType : PathType.values()) {
            graphs.put(pathType, Graph.of(lines, stationMatcher, pathType));
        }
    }

    public PathResponse getPath(Long source, Long target, PathType pathType) {
        try {
            Graph graph = graphs.get(pathType);
            Path path = graph.getPath(source, target);
            List<Long> stationIds = path.getVertexList();
            List<LineStationEdge> edges = path.getEdgeList();
            List<StationResponse> responses = getStationResponses(stationIds);
            int distance = getDistance(edges);
            int duration = getDuration(edges);
            return PathResponse.of(responses, duration, distance);
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
