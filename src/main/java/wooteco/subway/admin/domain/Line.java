package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Line {
    @Id
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Embedded.Empty
    private Edges edges = new Edges(new HashSet<>());

    private Line() {
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.id = id;
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
        return edges.getEdges();
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
        edges.add(edge);
    }

    public void removeLineStationById(Long stationId) {
        edges.remove(stationId);
    }

    public List<Long> getLineStationsId() {
        List<Edge> list = new ArrayList<>();
        edges.findByPreStationId(null)
                .ifPresent(list::add);

        for (int i = 0; i < edges.size() - 1; i++) {
            list.add(findNext(list.get(i)));
        }

        return list.stream()
                .map(Edge::getStationId)
                .collect(Collectors.toList());
    }

    private Edge findNext(Edge nextStation) {
        return edges.findByPreStationId(nextStation.getStationId())
                .orElse(nextStation);
    }

    public boolean containsAll(final List<Long> stationIds) {
        return edges.containsStationIdAll(stationIds);
    }
}
