package wooteco.subway.ui.dto;

import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.SectionServiceRequest;

public class SectionRequest {

    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @NotNull
    private Integer distance;

    public SectionRequest() {
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

    public SectionServiceRequest toServiceRequest(Long lindId) {
        return new SectionServiceRequest(lindId, upStationId, downStationId, distance);
    }
}
