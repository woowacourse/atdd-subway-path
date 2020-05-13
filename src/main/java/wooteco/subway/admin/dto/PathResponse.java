package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;
    private Long duration;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Long distance, Long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }
}
