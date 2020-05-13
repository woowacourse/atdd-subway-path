package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
    List<StationResponse> stations;
    Long distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
