package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.PathInfo;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int duration;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int duration, int distance) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
    }

    public static PathResponse from(PathInfo pathInfo) {
        return new PathResponse(StationResponse.listOf(pathInfo.getPath()), pathInfo.getDuration(), pathInfo.getDistance());
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
