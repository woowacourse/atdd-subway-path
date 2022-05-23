package wooteco.subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Line line;
    private final long upStationId;
    private final long downStationId;
    private final Integer distance;

    public Section(Long id, Line line, long upStationId, long downStationId, Integer distance) {
        this.id = id;
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(Line line, long upStationId, long downStationId, Integer distance) {
        this(null, line, upStationId, downStationId, distance);
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

    public Line getLine() {
        return line;
    }

    public Long getLineId() {
        return line.getId();
    }

    public boolean matchDownStationId(Long id) {
        return downStationId == id;
    }

    public boolean mathUpStationId(long id) {
        return upStationId == id;
    }

    public int subtractDistance(int distance) {
        return this.distance - distance;
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
