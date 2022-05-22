package wooteco.subway.dao.entity;

public class SectionEntity {
    private final Long id;
    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    public SectionEntity(Long id, long lineId, long upStationId, long downStationId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(Long lineId, long upStationId, long downStationId, int distance) {
        this(0L, lineId, upStationId, downStationId, distance);
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
}
