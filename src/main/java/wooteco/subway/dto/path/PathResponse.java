package wooteco.subway.dto.path;

import java.util.List;
import wooteco.subway.dto.station.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final long distance;
    private final int fare;

    public PathResponse(List<StationResponse> stations, long distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
