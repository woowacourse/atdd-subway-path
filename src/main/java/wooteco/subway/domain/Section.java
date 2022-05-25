package wooteco.subway.domain;

import java.util.List;

public class Section {

    private final Long id;
    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final Long lineOrder;

    public Section(final Long id, final long lineId,
                   final long upStationId, final long downStationId,
                   final int distance, final Long lineOrder) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineOrder = lineOrder;
    }

    public static Section createWithoutId(final long lineId, final long upStationId, final long downStationId,
                                          final int distance, final Long lineOrder) {
        return new Section(null, lineId, upStationId, downStationId, distance, lineOrder);
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

    public List<Long> getAllStations() {
        return List.of(upStationId, downStationId);
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineOrder() {
        return lineOrder;
    }

    public boolean isSameId(final Long id) {
        return this.id.equals(id);
    }

    public boolean isSameUpStationId(final long stationId) {
        return upStationId == stationId;
    }

    public boolean isSameDownStationId(final long stationId) {
        return downStationId == stationId;
    }

    public boolean hasStationId(final long stationId) {
        return upStationId == stationId || downStationId == stationId;
    }

    public long getOppositeStation(final long stationId) {
        if (upStationId == stationId) {
            return downStationId;
        }
        return upStationId;
    }
}
