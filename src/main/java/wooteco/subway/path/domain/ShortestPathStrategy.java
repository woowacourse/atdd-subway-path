package wooteco.subway.path.domain;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public enum ShortestPathStrategy {
    DIJKSTRA{
        @Override
        public ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(List<Line> lines) {
            return new DijkstraShortestPath<>(new PathGraph(lines).graph());
        }
    },

    FloydWarshall{
        @Override
        public ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(List<Line> lines) {
            return new FloydWarshallShortestPaths<>(new PathGraph(lines).graph());
        }
    };

    public abstract ShortestPathAlgorithm<Station, DefaultWeightedEdge> match(List<Line> lines);
}
