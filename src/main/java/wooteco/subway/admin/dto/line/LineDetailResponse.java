package wooteco.subway.admin.dto.line;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.station.StationResponse;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class LineDetailResponse {
    private Long id;
    private String name;
    private String backgroundColor;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StationResponse> stations;

    public LineDetailResponse() {
    }

    public LineDetailResponse(Long id, String name, String backgroundColor,
        LocalTime startTime, LocalTime endTime, int intervalTime, LocalDateTime createdAt,
        LocalDateTime updatedAt, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stations = StationResponse.listOf(stations);
    }

    public static LineDetailResponse of(Line line, List<Station> stations) {
        return new LineDetailResponse(line.getId(), line.getName(), line.getBackgroundColor(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(), line.getCreatedAt(), line.getUpdatedAt(), stations);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBackgroundColor() {
        return backgroundColor;
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
