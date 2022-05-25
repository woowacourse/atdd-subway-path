package wooteco.subway.dto;

import javax.validation.constraints.NotNull;

public class DeleteSectionRequest {

    @NotNull(message = "제거할 역 ID가 필요합니다.")
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
