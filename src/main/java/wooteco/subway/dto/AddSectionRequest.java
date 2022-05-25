package wooteco.subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddSectionRequest {

    @NotNull(message = "상행역 ID가 필요합니다.")
    private Long upStationId;
    @NotNull(message = "하행역 ID가 필요합니다.")
    private Long downStationId;
    @Min(value = 1, message = "거리는 1이상이여야 합니다.")
    private int distance;

    public AddSectionRequest() {
    }

    public AddSectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
