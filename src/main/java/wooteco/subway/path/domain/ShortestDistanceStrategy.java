package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class ShortestDistanceStrategy implements PathStrategy {
    @Override
    public GraphPath<Station, DefaultWeightedEdge> calculateShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                                                         List<Station> stations, Station source, Station target) {

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target);
    }
}
