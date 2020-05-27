package wooteco.subway.admin.dto;

import javax.validation.constraints.NotNull;

public class LineStationCreateRequest {
    @NotNull(message = "이전역을 작성해주세요!")
    private Long preStationId;
    @NotNull(message = "현재역을 작성해주세요!")
    private Long stationId;
    @NotNull(message = "거리를 작성해주세요!")
    private Integer distance;
    @NotNull(message = "배차 시간을 작성해주세요!")
    private Integer duration;

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
