package wooteco.subway.admin.dto.request;

import javax.validation.constraints.NotNull;

public class LineStationCreateRequest {
    private Long preStationId;
    @NotNull(message = "다음역을 입력해주세요.")
    private Long stationId;
    @NotNull(message = "거리를 입력해주세요.")
    private int distance;
    @NotNull(message = "소요시간을 입력해주세요.")
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
