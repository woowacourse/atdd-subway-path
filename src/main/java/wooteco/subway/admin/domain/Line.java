package wooteco.subway.admin.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class Line extends BaseTime {
    @Id
    private Long id;
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private Set<LineStation> stations;

    public Line(Long id, String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime,
        Set<LineStation> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.stations = stations;
    }

    public static Line of(Long id, String name, String color, LocalTime startTime, LocalTime endTime,
        int intervalTime) {
        return new Line(id, name, color, startTime, endTime, intervalTime, new HashSet<>());
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
            .orElseThrow(RuntimeException::new);

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
            .orElseThrow(RuntimeException::new);

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

    private void validateLineStation(LineStation lineStation) {
        validateStation(lineStation);
        validateStations(lineStation);
        validateAlreadyRegistered(lineStation);
        validateHavingSame(lineStation);
    }

    private void validateStation(LineStation lineStation) {
        if (Objects.isNull(lineStation.getStationId())) {
            throw new IllegalArgumentException("현재역은 비어있을 수 없습니다.");
        }
    }

    private void validateAlreadyRegistered(LineStation lineStation) {
        if (!lineStation.isFirstLineStation() && stations.isEmpty()) {
            throw new IllegalArgumentException("첫 노선을 먼저 등록해야 합니다.");
        }
    }

    private void validateStations(LineStation lineStation) {
        if (lineStation.getStationId().equals(lineStation.getPreStationId())) {
            throw new IllegalArgumentException("같은 역을 출발지점과 도착지점으로 정할 수 없습니다.");
        }
    }

    private void validateHavingSame(LineStation lineStation) {
        for (LineStation station : stations) {
            if (station.hasSameStations(lineStation)) {
                throw new IllegalArgumentException("이미 등록된 구간입니다.");
            }
        }
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
}
