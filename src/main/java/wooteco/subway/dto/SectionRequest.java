package wooteco.subway.dto;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class SectionRequest {

    @NotNull
    private Long upStationId;

    @NotNull
    private Long downStationId;

    @Min(0)
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
}
