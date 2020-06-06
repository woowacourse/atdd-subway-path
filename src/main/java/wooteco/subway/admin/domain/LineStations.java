package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.admin.exception.DisconnectedStationException;
import wooteco.subway.admin.exception.NotFoundStationException;

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
        detachTargetLineStation(stationId, targetLineStation);

        lineStations.remove(targetLineStation);
    }

    private void detachTargetLineStation(Long stationId, LineStation targetLineStation) {
        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), stationId))
            .findFirst()
            .ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));
    }

    private LineStation findTargetLineStation(Long stationId) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getStationId(), stationId))
            .findFirst()
            .orElseThrow(() -> new NotFoundStationException(stationId));
    }

    public List<LineStation> getLineStations() {
        if (lineStations.isEmpty()) {
            return new ArrayList<>();
        }

        return sortedLineStations();
    }

    public List<Long> getLineStationsId() {
        if (lineStations.isEmpty()) {
            return new ArrayList<>();
        }

        return sortedLineStations().stream()
            .map(LineStation::getStationId)
            .collect(Collectors.toList());
    }

    private List<LineStation> sortedLineStations() {
        LineStation firstLineStation = findFirstLineStation();

        List<LineStation> stations = new ArrayList<>();
        stations.add(firstLineStation);

        while (stations.size() != lineStations.size()) {
            Long lastStationId = stations.get(stations.size() - 1).getStationId();
            LineStation nextLineStation = findNextLineStation(lastStationId);
            stations.add(nextLineStation);
        }

        return stations;
    }

    private LineStation findNextLineStation(Long lastStationId) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
            .findFirst()
            .orElseThrow(() -> new DisconnectedStationException(lastStationId));
    }

    private LineStation findFirstLineStation() {
        return lineStations.stream()
            .filter(it -> it.getPreStationId() == null)
            .findFirst()
            .orElseThrow(NotFoundStationException::new);
    }

    public Set<LineStation> getLineStation() {
        return lineStations;
    }
}
