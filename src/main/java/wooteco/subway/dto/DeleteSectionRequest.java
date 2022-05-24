package wooteco.subway.dto;

import javax.validation.constraints.NotNull;

public class DeleteSectionRequest {

    @NotNull(message = "삭제할 역은 빈 값일 수 없습니다.")
    private Long stationId;

    public DeleteSectionRequest() {
    }

    public DeleteSectionRequest(Long stationId) {
        this.stationId = stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
