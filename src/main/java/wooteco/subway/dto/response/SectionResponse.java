package wooteco.subway.dto.response;

import wooteco.subway.domain.section.Section;

public class SectionResponse {
    private final long id;
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    private SectionResponse(long id, long upStationId, long downStationId, int distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionResponse(Section section) {
        this(section.getId(), section.getUpStation().getId(), section.getDownStation().getId(),
                section.getDistance().getValue());
    }

    public long getId() {
        return id;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "SectionResponse{" +
                "id=" + id +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                '}';
    }
}
