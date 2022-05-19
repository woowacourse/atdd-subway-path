package wooteco.subway.dto;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(List<StationResponse> stationResponses, int distance, int fare) {
        this.stations = stationResponses;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
