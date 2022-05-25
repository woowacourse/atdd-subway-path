package wooteco.subway.dto;

public class SectionDto {

    private long id;
    private long lineId;
    private long upStationId;
    private long downStationId;
    private int distance;

    public SectionDto(final long id, final long lineId, final long upStationId, final long downStationId,
                      final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
