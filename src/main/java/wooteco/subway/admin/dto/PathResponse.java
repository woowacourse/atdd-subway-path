package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Station;

import java.util.List;

public class PathResponse {
    List<StationResponse> stations;
    int distance;
    int duration;

    public PathResponse(List<Station> stations, int distance, int duration) {
        this.stations = StationResponse.listOf(stations);
        this.distance = distance;
        this.duration = duration;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
