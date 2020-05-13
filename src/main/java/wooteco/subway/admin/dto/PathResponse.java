package wooteco.subway.admin.dto;

import java.util.List;
import java.util.Objects;

import wooteco.subway.admin.domain.Station;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;
    private Long duration;

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, Long distance, Long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse of(List<Station> stations, Long distance, Long duration) {
        List<StationResponse> stationResponses = StationResponse.listOf(stations);
        return new PathResponse(stationResponses, distance, duration);
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
