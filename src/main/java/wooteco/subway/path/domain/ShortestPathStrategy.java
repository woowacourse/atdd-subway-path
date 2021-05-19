package wooteco.subway.path.domain;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.station.domain.Station;

public enum ShortestPathStrategy {

    DIJKSTRA {
        @Override
        public ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(final PathGraph pathGraph) {
            return new DijkstraShortestPath<>(pathGraph.getGraph());
        }
    },
    FLOYD_WARSHALL {
        @Override
        public ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(final PathGraph pathGraph) {
            return new FloydWarshallShortestPaths<>(pathGraph.getGraph());
        }
    };

    public abstract ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(final PathGraph pathGraph);
}
