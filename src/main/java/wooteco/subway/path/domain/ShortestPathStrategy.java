package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.station.domain.Station;

public enum ShortestPathStrategy {
    DIJKSTRA {
        @Override
        public ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(WeightedGraph<Station, DefaultWeightedEdge> graph) {
            return new DijkstraShortestPath<>(graph);
        }
    };

    public abstract ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(WeightedGraph<Station, DefaultWeightedEdge> graph);
}
