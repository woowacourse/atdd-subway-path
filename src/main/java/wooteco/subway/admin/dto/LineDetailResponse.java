package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Edges;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LineDetailResponse {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StationResponse> stations;

    public LineDetailResponse() {
    }

    public LineDetailResponse(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime, LocalDateTime createdAt, LocalDateTime updatedAt, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stations = StationResponse.listOf(stations);
    }

    public static LineDetailResponse of(Line line, List<Station> stations) {
        return new LineDetailResponse(line.getId(), line.getName(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(), line.getCreatedAt(), line.getUpdatedAt(), stations);
    }

    public static List<LineDetailResponse> listOf(final Lines lines, final Stations stations) {
        List<LineDetailResponse> lineDetailResponses = new ArrayList<>();
        for (Line line : lines) {
            Edges edges = new Edges(line.getEdges());
            List<Long> edgesStationIds = edges.getStationIds();
            List<Station> stationResponses = stations.findAllById(edgesStationIds);
            lineDetailResponses.add(LineDetailResponse.of(line, stationResponses));
        }
        return lineDetailResponses;
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
