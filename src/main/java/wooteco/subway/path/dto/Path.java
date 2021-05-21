package wooteco.subway.path.dto;

import java.util.List;

import wooteco.subway.path.infrastructure.ShortestPath;
import wooteco.subway.station.domain.Station;

public class Path {
    private final List<Station> stations;
    private final int distance;

    private Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public Path(ShortestPath shortestPath, Station source, Station target) {
        this(shortestPath.getStations(source, target), shortestPath.getDistance(source, target));
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
