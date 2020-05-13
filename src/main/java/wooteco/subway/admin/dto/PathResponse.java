package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
    private Integer distance;
    private Integer duration;
    private List<String> path;

    public PathResponse() {
    }

    public PathResponse(Integer distance, Integer duration, List<String> path) {
        this.distance = distance;
        this.duration = duration;
        this.path = path;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public List<String> getPath() {
        return path;
    }
}
