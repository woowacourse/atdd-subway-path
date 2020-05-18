package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LineStations {
    private Set<LineStation> stations;

    public LineStations(Set<LineStation> stations) {
        this.stations = stations;
    }

    public static LineStations create() {
        return new LineStations(new HashSet<>());
    }

    public Set<LineStation> getLineStations() {
        return Collections.unmodifiableSet(stations);
    }

    public void add(LineStation lineStation) {
        stations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
            .findAny()
            .ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

        stations.add(lineStation);
    }

    public void removeByStationId(Long stationId) {
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

    public List<Station> getMatchingStations(List<Station> wholeStations) {
        List<Long> stationIds = stations.stream()
            .map(LineStation::getStationId)
            .collect(Collectors.toList());

        return wholeStations.stream()
            .filter(station -> stationIds.contains(station.getId()))
            .collect(Collectors.toList());
    }
}
