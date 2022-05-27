package wooteco.subway.ui.dto;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class SectionRequest {

    private Long upStationId;
    private Long downStationId;
    private int distance;

    private SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionRequest from(LineCreateRequest lineCreateRequest) {
        return new SectionRequest(lineCreateRequest.getUpStationId(), lineCreateRequest.getDownStationId(),
                lineCreateRequest.getDistance());
    }

    public Section toEntity(Long lineId, Station upStation, Station downStation) {
        return new Section(0L, lineId, upStation, downStation, distance);
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

    @Override
    public String toString() {
        return "SectionRequest{" +
                "upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                '}';
    }
}
