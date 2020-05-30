package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private double distance;
    private int duration;

    public PathResponse() {
    }

    public PathResponse(final List<StationResponse> stations, final double distance, final int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
