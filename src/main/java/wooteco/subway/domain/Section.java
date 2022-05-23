package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Section {

    private final Long id;
    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final Long lineOrder;

    public Section(Long id, long lineId, long upStationId, long downStationId, int distance, Long lineOrder) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineOrder = lineOrder;
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

    public boolean isSameUpStationId(long stationId) {
        return upStationId == stationId;
    }

    public boolean isSameDownStationId(long stationId) {
        return downStationId == stationId;
    }

    public boolean hasStationId(long stationId) {
        return upStationId == stationId || downStationId == stationId;
    }

    public long getOppositeStation(long stationId) {
        if(upStationId == stationId){
            return downStationId;
        }
        return upStationId;
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

    public List<Long> getAllStations(){
        return List.of(upStationId, downStationId);
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineOrder() {
        return lineOrder;
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
        return lineId == section.lineId && upStationId == section.upStationId && downStationId == section.downStationId
                && distance == section.distance && Objects.equals(id, section.id) && Objects.equals(
                lineOrder, section.lineOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineId, upStationId, downStationId, distance, lineOrder);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                ", lineOrder=" + lineOrder +
                '}';
    }
}
