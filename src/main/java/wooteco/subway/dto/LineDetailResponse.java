package wooteco.subway.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class LineDetailResponse {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private String bgColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StationResponse> stations;

    public LineDetailResponse() {
    }

    public LineDetailResponse(Long id, String name, LocalTime startTime, LocalTime endTime,
        int intervalTime, String bgColor, LocalDateTime createdAt,
        LocalDateTime updatedAt, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.bgColor = bgColor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stations = StationResponse.listOf(stations);
    }

    public static LineDetailResponse of(Line line, List<Station> stations) {
        return new LineDetailResponse(line.getId(), line.getName(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(),
            line.getBgColor(), line.getCreatedAt(), line.getUpdatedAt(), stations);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getBgColor() {
        return bgColor;
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
