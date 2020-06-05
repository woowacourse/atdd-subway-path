package wooteco.subway.admin.domain;

import java.util.Objects;

public class LineStation extends BaseTime {
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    private LineStation(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public static LineStation of(Long preStationId, Long stationId, int distance, int duration) {
        return new LineStation(preStationId, stationId, distance, duration);
    }

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
    }

    public boolean isFirstLineStation() {
        return Objects.isNull(preStationId);
    }

    public boolean hasSameStations(LineStation lineStation) {
        return (Objects.equals(this.stationId, lineStation.stationId) && Objects.equals(
            this.preStationId, lineStation.preStationId))
            || (Objects.equals(this.preStationId, lineStation.stationId));
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
