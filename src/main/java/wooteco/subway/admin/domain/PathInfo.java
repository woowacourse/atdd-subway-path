package wooteco.subway.admin.domain;

import java.util.List;

public class PathInfo {
    private List<Station> path;
    private int duration;
    private int distance;

    public PathInfo(List<Station> path, int duration, int distance) {
        this.path = path;
        this.duration = duration;
        this.distance = distance;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }
}
