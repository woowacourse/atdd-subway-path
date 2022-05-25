package wooteco.subway.ui.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import wooteco.subway.service.dto.SectionServiceRequest;

public class SectionRequest {

    @NotNull(message = "상행종점 id기 필요합니다.")
    private Long upStationId;

    @NotNull(message = "하행종점 id기 필요합니다.")
    private Long downStationId;

    @Positive(message = "거리는 1 이상이어야 합니다.")
    private int distance;

    private SectionRequest() {
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

    public SectionServiceRequest toServiceRequest(Long lindId) {
        return new SectionServiceRequest(lindId, upStationId, downStationId, distance);
    }
}
