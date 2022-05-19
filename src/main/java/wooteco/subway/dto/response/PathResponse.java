package wooteco.subway.dto.response;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

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
