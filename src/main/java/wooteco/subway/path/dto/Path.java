package wooteco.subway.path.dto;

import java.util.List;

import wooteco.subway.path.infrastructure.ShortestPath;
import wooteco.subway.station.domain.Station;

public class Path {
    private final ShortestPath shortestPath;

    public Path(ShortestPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Station> getStations() {
        return shortestPath.getStations();
    }

    public int getDistance() {
        return shortestPath.getDistance();
    }
}
