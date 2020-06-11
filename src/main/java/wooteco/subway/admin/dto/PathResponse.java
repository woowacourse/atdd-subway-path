package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
    private int distance;
    private int duration;
    private List<String> path;

    public PathResponse() {

    }

    public PathResponse(int distance, int duration, List<String> path) {
        this.distance = distance;
        this.duration = duration;
        this.path = path;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getPath() {
        return path;
    }
}
