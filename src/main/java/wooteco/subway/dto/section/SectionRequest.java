package wooteco.subway.dto.section;

import wooteco.subway.domain.Section;
import wooteco.subway.dto.line.LineRequest;

public class SectionRequest {

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionRequest(Section section) {
        upStationId = section.getUpStationId();
        downStationId = section.getDownStationId();
    }

    public SectionRequest(LineRequest lineRequest) {
        this.upStationId = lineRequest.getUpStationId();
        this.downStationId = lineRequest.getDownStationId();
        this.distance = lineRequest.getDistance();
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
