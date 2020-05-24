package wooteco.subway.admin.line.service.dto.line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import wooteco.subway.admin.line.domain.line.Line;
import wooteco.subway.admin.station.domain.Station;
import wooteco.subway.admin.station.service.dto.StationResponse;

public class LineDetailResponse {

    private Long id;
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StationResponse> stations;

    public LineDetailResponse() {
    }

    public LineDetailResponse(Long id, String name, String color, LocalTime startTime, LocalTime endTime,
        int intervalTime, LocalDateTime createdAt, LocalDateTime updatedAt, List<StationResponse> stations) {
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

    public static LineDetailResponse of(Line line, List<Station> stations) {
        return new LineDetailResponse(line.getId(), line.getName(), line.getColor(),
            line.getStartTime(), line.getEndTime(), line.getIntervalTime(), line.getCreatedAt(),
            line.getUpdatedAt(), StationResponse.listOf(stations));
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

    public List<StationResponse> getStations() {
        return stations;
    }

}
