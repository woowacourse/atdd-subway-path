package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final SubwayPathStrategy strategy;

    public Path(List<Station> stations, List<Section> sections) {
        this(new DijkstraSubwayPathStrategy(stations, sections));
    }

    public Path(SubwayPathStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Station> shortestPath(Station source, Station target) {
        return strategy.shortestPath(source, target);
    }

    public int shortestDistance(Station source, Station target) {
        return strategy.shortestDistance(source, target);
    }
}
