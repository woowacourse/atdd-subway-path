package wooteco.subway.dto;

import wooteco.subway.domain.Station;

import java.util.List;
import java.util.Set;

public class PathResponse {

    private List<Station> stations;
    private int distance;
    private double fare;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance, double fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public double getFare() {
        return fare;
    }
}
