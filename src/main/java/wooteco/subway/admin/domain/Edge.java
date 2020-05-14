package wooteco.subway.admin.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

public class Edge {
    @Id
    private Long id;
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    @PersistenceConstructor
    public Edge(final Long id, final Long preStationId, final Long stationId, final int distance, final int duration) {
        this.id = id;
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Edge(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public boolean isNotFirst() {
        return preStationId != null;
    }

    public boolean isSameStationId(Long stationId) {
        return this.stationId.equals(stationId);
    }

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
    }


    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

}
