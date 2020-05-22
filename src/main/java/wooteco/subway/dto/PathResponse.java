package wooteco.subway.dto;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse(List<StationResponse> stations, double distance, double duration) {
        this.stations = stations;
        this.distance = (int)distance;
        this.duration = (int)duration;
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
