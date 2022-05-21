package wooteco.subway.dto.response;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return new ArrayList<>(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
