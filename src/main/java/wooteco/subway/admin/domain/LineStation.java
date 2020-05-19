package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class LineStation {
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LineStation(Long preStationId, Long stationId, int distance, int duration,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static LineStation of(Long preStationId, Long stationId, int distance, int duration) {
        return new LineStation(preStationId, stationId, distance, duration, LocalDateTime.now(),
            LocalDateTime.now());
    }

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isFirstLineStation() {
        return Objects.isNull(preStationId);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean hasSameStations(LineStation lineStation) {
        return isSameWith(lineStation) || isNotCircular(lineStation);
    }

    private boolean isSameWith(LineStation lineStation) {
        return Objects.equals(this.stationId, lineStation.stationId) && Objects.equals(
            this.preStationId, lineStation.preStationId);
    }

    private boolean isNotCircular(LineStation lineStation) {
        return Objects.equals(this.preStationId, lineStation.stationId);
    }

}
