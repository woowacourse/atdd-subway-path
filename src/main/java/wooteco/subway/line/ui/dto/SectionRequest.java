package wooteco.subway.line.ui.dto;

import wooteco.subway.line.ui.dto.valid.NumberValidation;

import java.beans.ConstructorProperties;

public class SectionRequest {

    @NumberValidation
    private final Long upStationId;
    @NumberValidation
    private final Long downStationId;
    @NumberValidation
    private final int distance;

    @ConstructorProperties({"upStationId", "downStationId", "distance"})
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
