package wooteco.subway.dto.section;

import wooteco.subway.domain.section.Section;

public class SectionRequest {

    private long upStationId;

    private long downStationId;

    private int distance;

    private SectionRequest() {
    }

    public SectionRequest(long upStationId, long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section toSectionWithLineId(Long lindId) {
        return new Section(lindId, upStationId, downStationId, distance);
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
