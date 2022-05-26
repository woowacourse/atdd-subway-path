package wooteco.subway.controller.dto.section;

import wooteco.subway.service.dto.section.SectionRequestDto;

import javax.validation.constraints.Positive;

public class SectionRequest {

    @Positive(message = "[ERROR] 상행선 ID는 양수입니다.")
    private Long upStationId;
    @Positive(message = "[ERROR] 하행선 ID는 양수입니다.")
    private Long downStationId;
    @Positive(message = "[ERROR] 거리는 양수입니다.")
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

    public SectionRequestDto toServiceRequest(Long lineId) {
        return new SectionRequestDto(lineId, upStationId, downStationId, distance);
    }
}
