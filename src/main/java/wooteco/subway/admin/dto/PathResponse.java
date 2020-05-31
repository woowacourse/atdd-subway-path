package wooteco.subway.admin.dto;

import java.util.List;

import wooteco.subway.admin.domain.Station;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse() {
    }

    public PathResponse(final List<StationResponse> stations, final int distance, final int duration) {
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
