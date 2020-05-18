package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.admin.exception.NoExistLineStationException;

public class LineStations {
    private Set<LineStation> lineStations = new HashSet<>();

    public LineStations() {
    }

    public LineStations(Set<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public void addLineStation(LineStation lineStation) {
        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
            .findAny()
            .ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

        lineStations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        LineStation targetLineStation = findTargetLineStation(stationId);

        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), stationId))
            .findFirst()
            .ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

        lineStations.remove(targetLineStation);
    }

    private LineStation findTargetLineStation(Long stationId) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getStationId(), stationId))
            .findFirst()
            .orElseThrow(() -> new NoExistLineStationException("노선에 존재하지 않는 역입니다."));
    }

    public List<LineStation> createSortedLineStations() {
        if (lineStations.isEmpty()) {
            return new ArrayList<>();
        }

        return sortedLineStations();
    }

    public List<Long> createSortedLineStationsId() {
        if (lineStations.isEmpty()) {
            return new ArrayList<>();
        }

        return sortedLineStations().stream()
            .map(LineStation::getStationId)
            .collect(Collectors.toList());
    }

    private List<LineStation> sortedLineStations() {
        List<LineStation> stations = new ArrayList<>();
        stations.add(findFirstLineStation());

        while (stations.size() != lineStations.size()) {
            Long lastStationId = stations.get(stations.size() - 1).getStationId();
            stations.add(findNextLineStation(lastStationId));
        }

        return stations;
    }

    private LineStation findFirstLineStation() {
        return lineStations.stream()
            .filter(it -> !it.isNotFirstLineStation())
            .findFirst()
            .orElseThrow(() -> new NoExistLineStationException("노선에 첫번째 역이 존재하지 않습니다."));
    }

    private LineStation findNextLineStation(Long lastStationId) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
            .findFirst()
            .orElseThrow(() -> new NoExistLineStationException("노선에 연결된 역이 존재하지 않습니다."));
    }

    public Set<LineStation> getLineStations() {
        return lineStations;
    }
}
