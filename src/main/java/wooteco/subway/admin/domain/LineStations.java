package wooteco.subway.admin.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class LineStations {
    private final Set<LineStation> stations;

    private LineStations(Set<LineStation> stations) {
        this.stations = Objects.requireNonNull(stations);
    }

    public static LineStations ofEmpty() {
        return new LineStations(new LinkedHashSet<>());
    }

    public void addLineStation(LineStation lineStation) {
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
            .filter(LineStation::isFirstLineStation)
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

    public List<Station> findStationsFrom(List<Station> stations) {
        return this.stations.stream()
            .map(lineStation -> lineStation.findMatchingStation(stations))
            .collect(toList());
    }

    public Set<LineStation> getStations() {
        return Collections.unmodifiableSet(stations);
    }
}
