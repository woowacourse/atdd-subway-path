package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class StationPathFinder {
    private final PathGraph pathGraph;

    public StationPathFinder(final PathGraph pathGraph) {
        this.pathGraph = pathGraph;
    }

    public List<Station> getShortestPath(final Station sourceStation, final Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(pathGraph.getGraph());
        return dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
    }

    public int getShortestDistance(final Station sourceStation, final Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(pathGraph.getGraph());
        return (int) dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
    }
}


