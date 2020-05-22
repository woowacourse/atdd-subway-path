package wooteco.subway.admin.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineStationCreateRequest {
    private Long preStationId;

    @NotNull
    private Long stationId;

    @Positive
    private int distance;

    @Positive
    private int duration;

    private LineStationCreateRequest() {
    }

    public LineStationCreateRequest(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
