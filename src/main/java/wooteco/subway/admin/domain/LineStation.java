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

    public LineStation() {
    }

    public LineStation(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isNotFirstLineStation() {
        return Objects.nonNull(preStationId);
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

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
        this.updatedAt = LocalDateTime.now();
    }
}
