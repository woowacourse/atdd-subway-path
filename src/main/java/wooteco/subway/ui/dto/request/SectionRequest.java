package wooteco.subway.ui.dto.request;

import javax.validation.constraints.Positive;
import wooteco.subway.service.dto.request.SectionServiceRequest;

public class SectionRequest {

    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @Positive
    private Long distance;

    private SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionServiceRequest toServiceRequest() {
        return new SectionServiceRequest(upStationId, downStationId, distance);
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
