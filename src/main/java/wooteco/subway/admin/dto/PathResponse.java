package wooteco.subway.admin.dto;

public class PathResponse {
    private Integer distance;
    private Integer duration;

    public PathResponse() {
    }

    public PathResponse(Integer distance, Integer duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

}
