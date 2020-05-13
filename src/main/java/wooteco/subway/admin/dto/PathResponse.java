package wooteco.subway.admin.dto;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PathResponse that = (PathResponse)o;
        return Objects.equals(stations, that.stations) &&
                Objects.equals(distance, that.distance) &&
                Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, duration);
    }
}
