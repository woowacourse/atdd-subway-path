package wooteco.subway.dto;

import java.util.List;
import wooteco.subway.domain.Fare;
import wooteco.subway.dto.station.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public PathResponse(List<StationResponse> stations, double distance, Fare fare) {
        this.stations = stations;
        this.distance = (int) distance;
        this.fare = fare.getFare();
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
