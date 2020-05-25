package wooteco.subway.admin.dto.domain;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class LineDto {
    private final Long id;
    private final String name;
    private final String color;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int intervalTime;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Set<LineStation> stations;

    public LineDto(Long id, String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime, LocalDateTime createdAt, LocalDateTime updatedAt, Set<LineStation> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stations = stations;
    }

    public static LineDto of(Line line) {
        return new LineDto(line.getId(), line.getName(), line.getColor(), line.getStartTime(), line.getEndTime(),
                line.getIntervalTime(), line.getCreatedAt(), line.getUpdatedAt(), line.getStations());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<LineStation> getStations() {
        return stations;
    }
}
