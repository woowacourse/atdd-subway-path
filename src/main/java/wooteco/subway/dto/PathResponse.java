package wooteco.subway.dto;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> stations;
    private final double distance;
    private final int fare;


    public PathResponse(List<StationResponse> stations, double distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
