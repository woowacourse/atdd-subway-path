package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;

public class Line {
    public static final int FIRST = 0;
    @Id
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Edge> stations = new LinkedList<>();

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

    public List<Edge> getStations() {
        return stations;
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

    public void addLineStation(Edge edge) {
        if (edge.isFirst()) {
            addFirst(edge);
            return;
        }

        if (hasNoSuchPreStation(edge)) {
            throw new NoSuchElementException("이전 역이 등록되지 않았습니다.");
        }

        Optional<Edge> nextStation = findNextStationBy(edge.getPreStationId());
        if (nextStation.isPresent()) {
            addBetweenTwo(edge, nextStation.get());
            return;
        }

        stations.add(edge);
    }

    private void addFirst(Edge edge) {
        stations.stream()
                .findFirst()
                .ifPresent(station -> station.updatePreLineStation(edge.getStationId()));
        stations.add(FIRST, edge);
    }

    private void addBetweenTwo(Edge edge, Edge nextStation) {
        nextStation.updatePreLineStation(edge.getStationId());
        int position = stations.indexOf(nextStation);
        stations.add(position, edge);
    }

    private boolean hasNoSuchPreStation(Edge edge) {
        return stations.stream()
                .map(Edge::getStationId)
                .noneMatch(id -> edge.getPreStationId().equals(id));
    }

    private Optional<Edge> findNextStationBy(Long stationId) {
        return stations.stream()
                .filter(station -> stationId.equals(station.getPreStationId()))
                .findFirst();
    }

    public void removeLineStationById(Long stationId) {
        Edge station = findStationBy(stationId);
        findNextStationBy(stationId)
                .ifPresent(nextStation -> nextStation.updatePreLineStation(station.getPreStationId()));
        stations.remove(station);
    }

    private Edge findStationBy(Long stationId) {
        return stations.stream()
                .filter(edge -> edge.getStationId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 노선에 등록되지 않은 역입니다."));
    }

    public List<Long> getStationIds() {
        return stations.stream()
                .map(Edge::getStationId)
                .collect(Collectors.toList());
    }
}