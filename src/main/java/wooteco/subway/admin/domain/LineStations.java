package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.StationNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class LineStations {
    public static final ArrayList<Long> EMPTY_IDS = new ArrayList<>();
    public static final Long FIRST_LINE_STATION_INDEX = null;

    private Set<LineStation> lineStations;

    private LineStations(Set<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public static LineStations from(List<Line> lines) {
        Set<LineStation> lineStations = lines.stream()
                .map(Line::getLineStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return new LineStations(lineStations);
    }

    public static LineStations empty() {
        return new LineStations(new HashSet<>());
    }

    public void add(LineStation lineStation) {
        updatePreLineStation(lineStation.getPreStationId(), lineStation.getStationId());

        lineStations.add(lineStation);
    }

    public void updatePreLineStation(Long originalId, Long modifiedStationId) {
        findPreStationIs(originalId)
                .ifPresent(it -> it.updatePreLineStation(modifiedStationId));
    }

    public void remove(LineStation lineStation) {
        lineStations.remove(lineStation);
    }

    public LineStation findLineStationBy(Long stationId) {
        return lineStations.stream()
                .filter(it -> Objects.equals(it.getStationId(), stationId))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(stationId));
    }

    public List<Long> getLineStationsId() {
        if (lineStations.isEmpty()) {
            return EMPTY_IDS;
        }

        LineStation firstLineStation = findPreStationIs(FIRST_LINE_STATION_INDEX)
                .orElseThrow(() -> new AssertionError("첫 역은 반드시 존재해야합니다."));

        return findLineStationsId(firstLineStation);
    }

    private List<Long> findLineStationsId(LineStation firstLineStation) {
        List<Long> stationIds = new ArrayList<>();
        stationIds.add(firstLineStation.getStationId());

        while (stationIds.size() < lineStations.size()) {
            Long lastStationId = stationIds.get(stationIds.size() - 1);
            LineStation nextLineStation = findPreStationIs(lastStationId)
                    .orElseThrow(() -> new AssertionError("이어지는 역이 반드시 존재해야합니다."));

            stationIds.add(nextLineStation.getStationId());
        }

        return stationIds;
    }

    public Stations getMatchingStations(Stations wholeStations) {
        List<Long> stationIds = lineStations.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toList());

        return wholeStations.getMatchingStationsByIds(stationIds);
    }

    private Optional<LineStation> findPreStationIs(Long preStationId) {
        return lineStations.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), preStationId))
                .findFirst();
    }

    public List<LineStation> getEdges() {
        return lineStations.stream()
                .filter(lineStation -> !Objects.equals(lineStation.getPreStationId(), FIRST_LINE_STATION_INDEX))
                .collect(Collectors.toList());
    }

    public Set<LineStation> getLineStations() {
        return lineStations;
    }
}
