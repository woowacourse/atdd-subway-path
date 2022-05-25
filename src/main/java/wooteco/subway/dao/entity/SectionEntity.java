package wooteco.subway.dao.entity;

public class SectionEntity {
    private final long id;
    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    public SectionEntity(long id, long lineId, long upStationId, long downStationId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionEntity(long lineId, long upStationId, long downStationId, int distance) {
        this(0, lineId, upStationId, downStationId, distance);
    }

    public long getId() {
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
