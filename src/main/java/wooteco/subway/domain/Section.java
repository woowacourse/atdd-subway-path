package wooteco.subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Long lineId;
    private final long upStationId;
    private final long downStationId;
    private final Integer distance;

    public Section(Long id, Long lineId, long upStationId, long downStationId, Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(long lineId, long upStationId, long downStationId, Integer distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public Section(long upStationId, long downStationId) {
        this(null, null, upStationId, downStationId, null);
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

    public boolean matchDownStationId(Long id) {
        return downStationId == id;
    }

    public boolean mathUpStationId(long id) {
        return upStationId == id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
