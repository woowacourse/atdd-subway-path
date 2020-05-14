package wooteco.subway.admin.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private List<LineStation> stations = new LinkedList<>();

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

    public List<LineStation> getStations() {
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

    public void addLineStation(LineStation lineStation) {
        if (lineStation.isFirst()) {
            addFirst(lineStation);
            return;
        }

        if (hasNoSuchPreStation(lineStation)) {
            throw new NoSuchElementException("이전 역이 등록되지 않았습니다.");
        }

        Optional<LineStation> nextStation = findNextStationBy(lineStation.getPreStationId());
        if (nextStation.isPresent()) {
            addBetweenTwo(lineStation, nextStation.get());
            return;
        }

        stations.add(lineStation);
    }

    private void addFirst(LineStation lineStation) {
        stations.stream()
                .findFirst()
                .ifPresent(station -> station.updatePreLineStation(lineStation.getStationId()));
        stations.add(FIRST, lineStation);
    }

    private void addBetweenTwo(LineStation lineStation, LineStation nextStation) {
        nextStation.updatePreLineStation(lineStation.getStationId());
        int position = stations.indexOf(nextStation);
        stations.add(position, lineStation);
    }

    private boolean hasNoSuchPreStation(LineStation lineStation) {
        return stations.stream()
                .map(LineStation::getStationId)
                .noneMatch(id -> lineStation.getPreStationId().equals(id));
    }

    private Optional<LineStation> findNextStationBy(Long stationId) {
        return stations.stream()
                .filter(station -> stationId.equals(station.getPreStationId()))
                .findFirst();
    }

    public void removeLineStationById(Long stationId) {
        LineStation station = findStationBy(stationId);
        findNextStationBy(stationId)
                .ifPresent(nextStation -> nextStation.updatePreLineStation(station.getPreStationId()));
        stations.remove(station);
    }

    private LineStation findStationBy(Long stationId) {
        return stations.stream()
                .filter(lineStation -> lineStation.getStationId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 노선에 등록되지 않은 역입니다."));
    }

    public List<Long> getStationIds() {
        return stations.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toList());
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
}