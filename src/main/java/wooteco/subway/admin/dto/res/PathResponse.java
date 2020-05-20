package wooteco.subway.admin.dto.res;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
