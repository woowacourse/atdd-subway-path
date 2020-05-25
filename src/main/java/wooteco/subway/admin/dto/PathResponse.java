package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.path.Path;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private Integer distance;
    private Integer duration;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Integer distance, Integer duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse of(Path path) {
        Stations stations = path.getStations();
        return new PathResponse(StationResponse.listOf(stations),
                path.getDistance(),
                path.getDuration());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}
