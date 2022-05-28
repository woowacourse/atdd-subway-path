package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Section {

    private static final int MIN_DISTANCE = 1;

    private final Long id;
    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    public Section(Long id, long lineId, long upStationId, long downStationId, int distance) {
        validate(upStationId, downStationId, distance);
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(long lineId, long upStationId, long downStationId, int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    private void validate(long upStationId, long downStationId, int distance) {
        if (upStationId == downStationId) {
            throw new IllegalArgumentException("상행 종점과 하행 종점은 같을 수 없습니다.");
        }
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException("역간의 거리는 1 이상이어야 합니다.");
        }
    }

    public List<Section> split(Section newSection) {
        int newDistance = validateDistance(newSection.getDistance());
        if (this.upStationId == newSection.getUpStationId()) {
            return List.of(newSection, new Section(lineId, newSection.getDownStationId(), downStationId, newDistance));
        }
        return List.of(new Section(lineId, upStationId, newSection.getUpStationId(), newDistance), newSection);
    }

    private int validateDistance(int distance) {
        if (distance >= this.distance) {
            throw new IllegalArgumentException("기존 구간의 길이보다 작아야합니다.");
        }
        return this.distance - distance;
    }

    public boolean contains(Long stationId) {
        return upStationId == stationId || downStationId == stationId;
    }

    public Section merge(Section section) {
        int totalDistance = this.distance + section.getDistance();
        if (this.downStationId == section.getUpStationId()) {
            return new Section(lineId, this.upStationId, section.getDownStationId(), totalDistance);
        }
        return new Section(lineId, section.getUpStationId(), this.downStationId, totalDistance);
    }

    public Long getId() {
        return id;
    }

    public long getLineId() {
        return lineId;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Section section = (Section) o;
        return lineId == section.lineId && upStationId == section.upStationId && downStationId == section.downStationId
                && distance == section.distance && Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineId, upStationId, downStationId, distance);
    }
}
