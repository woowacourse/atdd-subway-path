package wooteco.subway.path.domain;

import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.station.domain.Station;

public class PathFinder {

    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm;

    public PathFinder(ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm) {
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public List<Station> shortestPath(Station source, Station target) {
        return shortestPathAlgorithm.getPath(source, target).getVertexList();
    }

    public int shortestPathDistance(Station source, Station target) {
        return (int) shortestPathAlgorithm.getPath(source, target).getWeight();
    }

}
