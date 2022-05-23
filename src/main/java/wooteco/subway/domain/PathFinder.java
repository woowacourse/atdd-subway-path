package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;

public class PathFinder {

    private Sections sections;
    private DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(Sections sections) {
        this.sections = sections;
        this.dijkstraShortestPath = initDijkstraShortestPath();
    }

    public List<Long> findPath(Long from, Long to) {
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public int findDistance(Long from, Long to) {
        return (int) dijkstraShortestPath.getPathWeight(from, to);
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> initDijkstraShortestPath() {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Set<Long> stationIds = sections.distinctStationIds();
        fillVertexes(graph, stationIds);
        fillEdges(graph);
        return new DijkstraShortestPath<>(graph);
    }

    private void fillVertexes(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Set<Long> stationIds) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private void fillEdges(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections.values()) {
            Long upStationId = section.getUpStationId();
            Long downStationId = section.getDownStationId();

            DefaultWeightedEdge edge = graph.addEdge(upStationId, downStationId);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
