package wooteco.subway.ui.dto.response;

import java.util.List;

public class RouteResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final Long fare;

    public RouteResponse(List<StationResponse> stations, int distance, Long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare;
    }
}
