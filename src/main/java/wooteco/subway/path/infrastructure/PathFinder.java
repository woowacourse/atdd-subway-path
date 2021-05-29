package wooteco.subway.path.infrastructure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.path.domain.Path;
import wooteco.subway.station.domain.Station;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public Path getShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);

        return new Path(path.getVertexList(), path.getWeight());
    }
}
