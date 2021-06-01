package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayMap {
    private final ShortestPath shortestPath;

    public SubwayMap(ShortestPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Station> shortestPath(Station source, Station target) {
        return shortestPath.find(source, target);
    }

    public int distance(Station source, Station target) {
        return shortestPath.calculate(source, target);
    }
}
