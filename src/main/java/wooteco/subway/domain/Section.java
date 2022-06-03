package wooteco.subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;

    public Section(Long id, Long lineId, Long upStationId, Long downStationId, Distance distance) {
        validateNull(upStationId, downStationId);
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private static void validateNull(Long upStationId, Long downStationId) {
        if (upStationId == null || downStationId == null) {
            throw new IllegalArgumentException("구간의 상행역과 하행역에 올바른 정보를 입력해주세요.");
        }
    }

    public Section(Long lineId, Long upStationId, Long downStationId, Distance distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public Section(Long upStationId, Long downStationId) {
        this(null, upStationId, downStationId, new Distance(1));
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Distance getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public boolean matchDownStationId(Long id) {
        return downStationId.equals(id);
    }

    public boolean mathUpStationId(Long id) {
        return upStationId.equals(id);
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
