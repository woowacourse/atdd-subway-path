package wooteco.subway.service.dto;

import java.util.List;

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
    public String toString() {
        return "PathResponse{" +
                "distance=" + distance +
                ", fare=" + fare +
                ", stations=" + stations +
                '}';
    }
}
