package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;

import wooteco.subway.admin.domain.LineStation;

public class LineStationCreateRequest {
    @NotBlank(message = "이전역을 작성해주세요!")
    private Long preStationId;
    @NotBlank(message = "현재역을 작성해주세요!")
    private Long stationId;
    @NotBlank(message = "거리를 작성해주세요!")
    private Integer distance;
    @NotBlank(message = "배차 시간을 작성해주세요!")
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

    public LineStation toLineStation(){
        return new LineStation(preStationId, stationId, distance, duration);
    }
}
