
package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.LineStations;

import java.util.List;


public class PathResponse {
    private List<StationResponse> stations;
    private int duration;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, LineStations lineStations, List<Long> path) {
        this.stations = stations;
        this.duration = lineStations.calculateFastestDuration(path);
        this.distance = lineStations.calculateShortestDistance(path);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }
}