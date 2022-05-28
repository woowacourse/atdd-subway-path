package wooteco.subway.infra;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

public class PathFinder {

    private final ShortestPathStrategy shortestPathStrategy;

    public PathFinder(ShortestPathStrategy shortestPathStrategy) {
        this.shortestPathStrategy = shortestPathStrategy;
    }

    public Path getPath(Station upStation, Station downStation) {
        ShortestPathAlgorithm shortestPath = shortestPathStrategy.createShortestPath();
        GraphPath<Station, ShortestPathEdge> path = shortestPath.getPath(upStation, downStation);

        return new Path(calculateStations(path), calculateDistance(path), calculateExtraFare(path));
    }

    private List<Station> calculateStations(GraphPath<Station, ShortestPathEdge> path) {
        return path.getVertexList();
    }

    private int calculateDistance(GraphPath<Station, ShortestPathEdge> path) {
        return (int) path.getWeight();
    }

    private int calculateExtraFare(GraphPath<Station, ShortestPathEdge> path) {
        return path.getEdgeList()
                .stream()
                .mapToInt(edge -> edge.getExtraFare())
                .max()
                .orElse(0);
    }
}
