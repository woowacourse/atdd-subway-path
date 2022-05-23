package wooteco.subway.domain;

import java.util.List;

public class Path {
    private final Lines lines;
    private final PathFindingStrategy pathFindingStrategy;
    private final Station source;
    private final Station target;

    public Path(Lines lines, PathFindingStrategy pathFindingStrategy, Station source,
        Station target) {
        this.lines = lines;
        this.pathFindingStrategy = pathFindingStrategy;
        this.source = source;
        this.target = target;
    }

    public int getShortestDistance() {
        return (int)pathFindingStrategy.getShortestDistance(source, target, lines);
    }

    public List<Station> getShortestPath() {
        return pathFindingStrategy.getShortestPath(source, target, lines);
    }
}
