package wooteco.subway.dto.section;

import java.util.Objects;

public class SectionResponse {

    private Long id;
    private final Long upStationId;
    private final Long downStationId;
    private int distance;

    public SectionResponse(Long id, Long upStationId, Long downStationId) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public SectionResponse(long id, long upStationId, long downStationId, int distance) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionResponse that = (SectionResponse) o;
        return distance == that.distance && Objects.equals(id, that.id) && Objects.equals(upStationId,
                that.upStationId) && Objects.equals(downStationId, that.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStationId, downStationId, distance);
    }
}
