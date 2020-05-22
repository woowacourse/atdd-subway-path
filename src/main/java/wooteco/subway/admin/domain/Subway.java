package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.exception.StationNotFoundException;

public class Subway {
    private final List<Line> lines;
    private final List<Station> stations;

    public Subway(List<Line> lines, List<Station> stations) {
        validateSubway(lines, stations);
        this.lines = lines;
        this.stations = stations;
    }

    private void validateSubway(List<Line> lines, List<Station> stations) {
        validateLines(lines);
        validateStations(stations);
    }

    private void validateLines(List<Line> lines) {
        if (Objects.isNull(lines)) {
            throw new LineNotFoundException();
        }
    }

    private void validateStations(List<Station> stations) {
        if (Objects.isNull(stations)) {
            throw new StationNotFoundException();
        }
    }

    private void validateStationName(String sourceName, String targetName) {
        if (sourceName.equals(targetName)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public Station findStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.isSameName(stationName))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(stationName));
    }

    private Map<Long, Station> generateStationMapper() {
        return stations.stream()
                .collect(Collectors.toMap(
                        Station::getId,
                        station -> station));
    }

    private List<LineStation> generateLineStations() {
        return lines.stream()
                .flatMap(line -> line.getLineStations().stream())
                .filter(LineStation::hasPreStation)
                .collect(Collectors.toList());
    }
}
