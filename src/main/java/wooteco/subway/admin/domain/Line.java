package wooteco.subway.admin.domain;

import org.springframework.data.annotation.Id;
import wooteco.subway.admin.service.errors.PathException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Line {
    @Id
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<LineStation> stations = new HashSet<>();

    public Line() {
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this(null, name, startTime, endTime, intervalTime);
    }

    public void update(Line line) {
        if (line.getName() != null) {
            this.name = line.getName();
        }
        if (line.getStartTime() != null) {
            this.startTime = line.getStartTime();
        }
        if (line.getEndTime() != null) {
            this.endTime = line.getEndTime();
        }
        if (line.getIntervalTime() != 0) {
            this.intervalTime = line.getIntervalTime();
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void addLineStation(LineStation lineStation) {
        stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
                .findAny()
                .ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

        stations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        LineStation targetLineStation = stations.stream()
                .filter(it -> Objects.equals(it.getStationId(), stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), stationId))
                .findFirst()
                .ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

        stations.remove(targetLineStation);
    }

    public List<Long> getLineStationsId() {
        List<Long> stationIds = new ArrayList<>();
        if (stations.isEmpty()) {
            return stationIds;
        }

        LineStation firstLineStation = getFirstLineStation();

        stationIds.add(firstLineStation.getStationId());
        for (int i = 1; i < stations.size(); i++) {
            Long lastStationId = stationIds.get(stationIds.size() - 1);

            LineStation nextLineStation = stations.stream()
                    .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
                    .findFirst()
                    .orElseThrow(() -> new PathException("역이 연결되있지 않습니다."));

            stationIds.add(nextLineStation.getStationId());
        }

        return stationIds;
    }

    private Optional<LineStation> getNextLineStation(Long lastStationId) {
        return stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
                .findFirst();
    }

    private LineStation getFirstLineStation() {
        return stations.stream()
                .filter(it -> it.getPreStationId() == null)
                .findFirst()
                .orElseThrow(RuntimeException::new);
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

    public Set<LineStation> getStations() {
        return stations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
