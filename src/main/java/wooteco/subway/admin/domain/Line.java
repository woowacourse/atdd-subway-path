package wooteco.subway.admin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.annotation.Id;

import wooteco.subway.admin.exception.DuplicatedValueException;
import wooteco.subway.admin.exception.NoSuchValueException;
import wooteco.subway.admin.exception.ValueRequiredException;

public class Line {
    @Id
    private Long id;
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<LineStation> stations = new HashSet<>();

    public Line(Long id, String name, String color, LocalTime startTime, LocalTime endTime,
        int intervalTime) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Line of(String name, String color, LocalTime startTime, LocalTime endTime,
        int intervalTime) {
        return new Line(null, name, color, startTime, endTime, intervalTime);
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

    public Set<LineStation> getStations() {
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
        if (line.getColor() != null) {
            this.color = line.getColor();
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
        validateLineStation(lineStation);
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
            .orElseThrow(() -> new NoSuchValueException("해당 노선에 존재하지 않는 구간입니다."));

        stations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), stationId))
            .findFirst()
            .ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

        stations.remove(targetLineStation);
    }

    public List<Long> getLineStationsId() {
        if (stations.isEmpty()) {
            return new ArrayList<>();
        }

        LineStation firstLineStation = stations.stream()
            .filter(it -> it.getPreStationId() == null)
            .findFirst()
            .orElseThrow(() -> new NoSuchValueException("해당 노선에 구간이 존재하지 않습니다."));

        List<Long> stationIds = new ArrayList<>();
        stationIds.add(firstLineStation.getStationId());

        while (true) {
            Long lastStationId = stationIds.get(stationIds.size() - 1);
            Optional<LineStation> nextLineStation = stations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
                .findFirst();

            if (!nextLineStation.isPresent()) {
                break;
            }
            stationIds.add(nextLineStation.get().getStationId());
        }

        return stationIds;
    }

    public Stream<Long> stationsIdStream() {
        return getLineStationsId().stream();
    }

    private void validateLineStation(LineStation lineStation) {
        validateStation(lineStation);
        validateStations(lineStation);
        validateAlreadyRegistered(lineStation);
        validateHavingSame(lineStation);
    }

    private void validateStation(LineStation lineStation) {
        if (Objects.isNull(lineStation.getStationId())) {
            throw new ValueRequiredException("현재역은 비어있을 수 없습니다.");
        }
    }

    private void validateAlreadyRegistered(LineStation lineStation) {
        if (!lineStation.isFirstLineStation() && stations.isEmpty()) {
            throw new ValueRequiredException("첫 노선을 먼저 등록해야 합니다.");
        }
    }

    private void validateStations(LineStation lineStation) {
        if (lineStation.getStationId().equals(lineStation.getPreStationId())) {
            throw new DuplicatedValueException("같은 역을 출발지점과 도착지점으로 정할 수 없습니다.");
        }
    }

    private void validateHavingSame(LineStation lineStation) {
        for (LineStation station : stations) {
            if (station.hasSameStations(lineStation)) {
                throw new DuplicatedValueException("이미 등록된 구간입니다.");
            }
        }
    }
}
