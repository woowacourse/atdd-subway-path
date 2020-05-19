package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import wooteco.subway.admin.exception.LineStationNotFoundException;

public class LineStations {
    private Set<LineStation> lineStations = new HashSet<>();

    LineStations() {
    }

    public Set<LineStation> getLineStations() {
        return lineStations;
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
                .orElseThrow(LineStationNotFoundException::new);

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
                .filter(it -> !it.hasPreStation())
                .findFirst()
                .orElseThrow(LineStationNotFoundException::new);

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
}
