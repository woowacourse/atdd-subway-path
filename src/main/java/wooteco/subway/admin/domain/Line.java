package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Embedded;

import wooteco.subway.admin.domain.vo.LineSchedule;
import wooteco.subway.admin.domain.vo.LineStations;

public class Line {
    @Id
    private Long id;
    private String name;
    private String color;
    @Embedded.Nullable
    private LineSchedule lineSchedule;
    @Embedded.Empty
    private LineStations lineStations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PersistenceConstructor
    public Line(Long id, String name, String color, LineSchedule lineSchedule,
        LineStations lineStations,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.lineSchedule = lineSchedule;
        this.lineStations = lineStations;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Line of(Long id, String name, String color, LocalTime startTime,
        LocalTime endTime,
        int intervalTime) {
        LineSchedule lineSchedule = LineSchedule.of(startTime, endTime, intervalTime);
        return new Line(id, name, color, lineSchedule, LineStations.empty(), LocalDateTime.now(),
            LocalDateTime.now());
    }

    public static Line withoutId(String name, String color, LocalTime startTime, LocalTime endTime,
        int intervalTime) {
        LineSchedule lineSchedule = LineSchedule.of(startTime, endTime, intervalTime);
        return new Line(null, name, color, lineSchedule, LineStations.empty(), LocalDateTime.now(),
            LocalDateTime.now());
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
        return lineSchedule.getStartTime();
    }

    public LocalTime getEndTime() {
        return lineSchedule.getEndTime();
    }

    public int getIntervalTime() {
        return lineSchedule.getIntervalTime();
    }

    public List<LineStation> getLineStations() {
        return lineStations.getStations();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(Line line) {
        if (line.getName() != null) {
            this.name = line.getName();
        }
        if (line.getColor() != null) {
            this.color = line.getColor();
        }
        if (line.lineSchedule != null) {
            this.lineSchedule = line.lineSchedule;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void addLineStation(LineStation lineStation) {
        lineStations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        lineStations.remove(stationId);
    }

    public List<Long> getLineStationsId() {
        return lineStations.getStationIds();
    }
}
