package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StationResponse;

public class Path {
    private final GraphPath<Long, LineStationEdge> path;

    public Path(GraphPath<Long, LineStationEdge> path) {
        this.path = path;
    }

    public PathResponse createPathResponse(Map<Long, Station> idToStation) {
        List<StationResponse> stationResponses = createStationResponses(idToStation);
        List<LineStationEdge> edges = path.getEdgeList();
        int distance = edges.stream()
            .mapToInt(LineStationEdge::getDistance)
            .sum();
        int duration = edges.stream()
            .mapToInt(LineStationEdge::getDuration)
            .sum();
        return new PathResponse(stationResponses, duration, distance);
    }

    private List<StationResponse> createStationResponses(Map<Long, Station> idToStation) {
        List<Long> stationIds = path.getVertexList();
        return stationIds.stream()
            .map(idToStation::get)
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }
}
