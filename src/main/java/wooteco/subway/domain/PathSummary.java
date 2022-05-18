package wooteco.subway.domain;

import java.util.List;

public class PathSummary {

    private final List<Station> path;
    private final int distance;
    private final int fare;

    public PathSummary(List<Station> path, int distance, int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
