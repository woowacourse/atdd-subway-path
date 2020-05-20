package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.graph.PathDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<String> stations;
    private Integer totalDistance;
    private Integer totalDuration;

    private PathResponse() {
    }

    public PathResponse(final List<String> stations, final Integer totalDistance, final Integer totalDuration) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
    }

    public static PathResponse of(PathDetail pathDetail, Stations stations) {
        List<String> names = pathDetail.getPaths().stream()
                .map(stations::findNameById)
                .collect(Collectors.toList());
        return new PathResponse(names, pathDetail.getTotalDistance(), pathDetail.getTotalDuration());
    }

    public List<String> getStations() {
        return new ArrayList<>(stations);
    }

    public Integer getTotalDistance() {
        return this.totalDistance;
    }

    public Integer getTotalDuration() {
        return this.totalDuration;
    }
}
