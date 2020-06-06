package wooteco.subway.admin.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LineStationCreateRequest {
    private Long preStationId;
    @NotNull
    private Long stationId;
    @Min(0)
    private int distance;
    @Min(0)
    private int duration;

    public LineStationCreateRequest() {
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
