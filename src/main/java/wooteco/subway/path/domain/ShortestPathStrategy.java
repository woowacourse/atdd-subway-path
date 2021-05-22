package wooteco.subway.path.domain;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.section.domain.Section;

import java.util.List;

public enum ShortestPathStrategy {
    DIJKSTRA {
        @Override
        public ShortestPathAlgorithm<Long, DefaultWeightedEdge> generateAlgorithm(List<Section> sections) {
            PathGraph pathGraph = new PathGraph(new WeightedMultigraph<>(DefaultWeightedEdge.class), sections);
            return new DijkstraShortestPath<>(pathGraph.getPathGraph());
        }
    };

    public abstract ShortestPathAlgorithm<Long, DefaultWeightedEdge> generateAlgorithm(List<Section> sections);
}
