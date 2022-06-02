package wooteco.subway.service.dto;

import java.util.List;
import java.util.Objects;

public class PathResponse {

    int distance;
    int fare;
    List<StationResponse> stations;

    private PathResponse() {
    }

    public PathResponse(int distance, int fare, List<StationResponse> stations) {
        this.distance = distance;
        this.fare = fare;
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathResponse response = (PathResponse) o;
        return distance == response.distance && fare == response.fare && Objects.equals(stations,
                response.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, fare, stations);
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "distance=" + distance +
                ", fare=" + fare +
                ", stations=" + stations +
                '}';
    }
}
