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

import wooteco.subway.admin.exception.StationNotFoundException;

public class Line {
    @Id
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private String bgColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<LineStation> lineStations = new HashSet<>();

    public Line() {
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime, String bgColor) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.bgColor = bgColor;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime, String bgColor) {
        this(null, name, startTime, endTime, intervalTime, bgColor);
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

    public Set<LineStation> getLineStations() {
        return lineStations;
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

    public void update(Line line) {
        if (Objects.nonNull(line.name)) {
            this.name = line.name;
        }
        if (Objects.nonNull(line.startTime)) {
            this.startTime = line.startTime;
        }
        if (Objects.nonNull(line.endTime)) {
            this.endTime = line.endTime;
        }
        if (line.intervalTime != 0) {
            this.intervalTime = line.intervalTime;
        }
        if (Objects.nonNull(line.bgColor)) {
            this.bgColor = line.getBgColor();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void addLineStation(LineStation lineStation) {
        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
            .findAny()
            .ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));
        lineStations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        LineStation targetLineStation = lineStations.stream()
            .filter(it -> Objects.equals(it.getStationId(), stationId))
            .findFirst()
            .orElseThrow(RuntimeException::new);

        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), stationId))
            .findFirst()
            .ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

        lineStations.remove(targetLineStation);
    }

    public List<Long> getLineStationsId() {
        if (lineStations.isEmpty()) {
            return new ArrayList<>();
        }

        LineStation firstLineStation = lineStations.stream()
            .filter(it -> Objects.isNull(it.getPreStationId()))
            .findFirst()
            .orElseThrow(RuntimeException::new);

        List<Long> stationIds = new ArrayList<>();
        stationIds.add(firstLineStation.getStationId());

        while (true) {
            Long lastStationId = stationIds.get(stationIds.size() - 1);
            Optional<LineStation> nextLineStation = lineStations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            stationIds.add(nextLineStation.get().getStationId());
        }

        return stationIds;
    }

    public List<Station> getStations(List<Station> stations) {
        return getLineStationsId().stream()
            .map(lineStation -> getStation(stations, lineStation))
            .collect(Collectors.toList());
    }

    private Station getStation(final List<Station> stations, final Long lineStation) {
        return stations.stream()
            .filter(station -> station.is(lineStation))
            .findAny()
            .orElseThrow(() -> new StationNotFoundException(lineStation));
    }
}
