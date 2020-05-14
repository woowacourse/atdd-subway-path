package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Station;

import java.util.List;

public class PathSearchResponse {
    private int duration;
    private int distance;
    private List<String> stations;

    public PathSearchResponse(int duration, int distance, List<String> stations) {
        this.duration = duration;
        this.distance = distance;
        this.stations = stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public List<String> getStations() {
        return stations;
    }
}
