package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Section> values) {
        this.dijkstraShortestPath = initDijkstraShortestPath(values);
    }

    public List<Long> findPath(Long from, Long to) {
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public int findDistance(Long from, Long to) {
        return (int) dijkstraShortestPath.getPathWeight(from, to);
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> initDijkstraShortestPath(List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        fillVertexesAndEdges(graph, sections);
        return new DijkstraShortestPath<>(graph);
    }

    private void fillVertexesAndEdges(WeightedMultigraph<Long, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            Long upStationId = section.getUpStationId();
            Long downStationId = section.getDownStationId();

            graph.addVertex(upStationId);
            graph.addVertex(downStationId);

            DefaultWeightedEdge edge = graph.addEdge(upStationId, downStationId);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
