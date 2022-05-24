package wooteco.subway.ui.dto;

import static wooteco.subway.ui.dto.LineCreationRequest.OMISSION_MESSAGE;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.SectionServiceRequest;

public class SectionRequest {

    @NotNull(message = "상행역" + OMISSION_MESSAGE)
    @Min(value = 1, message = "상행역" + OMISSION_MESSAGE)
    private Long upStationId;
    @NotNull(message = "하행역" + OMISSION_MESSAGE)
    @Min(value = 1, message = "하행역" + OMISSION_MESSAGE)
    private Long downStationId;
    @NotNull(message = "구간의 거리" + OMISSION_MESSAGE)
    @Min(value = 1, message = "구간의 거리" + OMISSION_MESSAGE)
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
