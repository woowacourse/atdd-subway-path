package wooteco.subway.repository.entity;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    public SectionEntity(Long id, Long lineId, long upStationId, long downStationId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(Long lineId, long upStationId, long downStationId, int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public SectionEntity(Long upStationId, Long downStationId) {
        this(null, null, upStationId, downStationId, 0);
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

    public long getLineId() {
        return lineId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionEntity section = (SectionEntity) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
