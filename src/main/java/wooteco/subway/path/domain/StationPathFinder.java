package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class StationPathFinder {
    private final PathGraph pathGraph;
    private final ShortestPathStrategy shortestPathStrategy;

    public StationPathFinder(final PathGraph pathGraph, final ShortestPathStrategy shortestPathStrategy) {
        this.pathGraph = pathGraph;
        this.shortestPathStrategy = shortestPathStrategy;
    }

    public List<Station> getShortestPath(final Station sourceStation, final Station targetStation) {
        return shortestPathStrategy.match(pathGraph).getPath(sourceStation, targetStation).getVertexList();
    }

    public int getShortestDistance(final Station sourceStation, final Station targetStation) {
        return (int) shortestPathStrategy.match(pathGraph).getPathWeight(sourceStation, targetStation);
    }
}


