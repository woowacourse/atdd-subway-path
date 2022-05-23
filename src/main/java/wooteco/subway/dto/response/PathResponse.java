package wooteco.subway.dto.response;

import java.util.List;

import wooteco.subway.domain.Fare;

public class PathResponse {
    private List<StationResponse> stationResponses;
    private double distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stationResponses, double distance, Fare fare) {
        this.stationResponses = stationResponses;
        this.distance = distance;
        this.fare = fare.getValue();
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
