package wooteco.subway.admin.domain;

import org.springframework.data.annotation.Id;
import wooteco.subway.admin.exception.StationNotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Line {
    @Id
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Edge> edges = new HashSet<>();

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

    public Set<Edge> getEdges() {
        return edges;
    }

    public Set<Edge> getEdgesExceptFirst() {
        return edges.stream()
                .filter(edge -> Objects.nonNull(edge.getPreStationId()))
                .collect(Collectors.toSet());
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

    public void addEdge(Edge edge) {
        edges.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), edge.getPreStationId()))
                .findAny()
                .ifPresent(it -> it.updatePreEdge(edge.getStationId()));

        edges.add(edge);
    }

    public void removeEdgeById(Long stationId) {
        Edge targetEdge = edges.stream()
                .filter(it -> Objects.equals(it.getStationId(), stationId))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);

        edges.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), stationId))
                .findFirst()
                .ifPresent(it -> it.updatePreEdge(targetEdge.getPreStationId()));

        edges.remove(targetEdge);
    }

    public List<Long> getSortedStationIds() {
        if (edges.isEmpty()) {
            return new ArrayList<>();
        }

        Edge firstEdge = edges.stream()
                .filter(it -> it.getPreStationId() == null)
                .findFirst()
                .orElseThrow(StationNotFoundException::new);

        List<Long> stationIds = new ArrayList<>();
        stationIds.add(firstEdge.getStationId());

        while (true) {
            Long lastStationId = stationIds.get(stationIds.size() - 1);
            Optional<Edge> nextEdge = edges.stream()
                    .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
                    .findFirst();

            if (!nextEdge.isPresent()) {
                break;
            }
            stationIds.add(nextEdge.get().getStationId());
        }
        return stationIds;
    }

    public List<Long> getStationIds() {
        return this.edges.stream()
                .map(Edge::getStationId)
                .collect(Collectors.toList());
    }
}
