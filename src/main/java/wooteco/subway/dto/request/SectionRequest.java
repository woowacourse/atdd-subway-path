package wooteco.subway.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotNull(message = "상행 종점은 필수입니다.")
    private Long upStationId;
    @NotNull(message = "하행 종점은 필수입니다.")
    private Long downStationId;
    @NotNull(message = "거리는 필수입니다.")
    @Min(value = 1, message = "거리는 0일 수 없습니다.")
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
