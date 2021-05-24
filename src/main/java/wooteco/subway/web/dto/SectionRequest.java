package wooteco.subway.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionRequest {
    @NotNull(message = "상행역을 입력해주세요.")
    private Long upStationId;
    @NotNull(message = "하행역을 입력해주세요.")
    private Long downStationId;
    @Positive(message = "올바르지 않은 거리 형식입니다.")
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
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

    public int getDistance() {
        return distance;
    }
}
