package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Path {
    private List<Station> path;
    private int totalDuration;
    private int totalDistance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Path() {
    }

    public Path(List<Station> path, int totalDistance, int totalDuration, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
