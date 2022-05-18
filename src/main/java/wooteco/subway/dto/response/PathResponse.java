package wooteco.subway.dto.response;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stationResponses;
    private double distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stationResponses, double distance, int fare) {
        this.stationResponses = stationResponses;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
