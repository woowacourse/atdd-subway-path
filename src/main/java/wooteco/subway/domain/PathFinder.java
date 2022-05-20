package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathFinder {

    private final List<Section> values;
    private DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Section> sections) {
        this.values = sections;
        this.dijkstraShortestPath = initDijkstraShortestPath(sections);
    }

    public List<Long> findPath(Long from, Long to) {
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public int findDistance(Long from, Long to) {
        return (int) dijkstraShortestPath.getPathWeight(from, to);
    }


    private DijkstraShortestPath<Long, DefaultWeightedEdge> initDijkstraShortestPath(List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Set<Long> stationIds = findNonDuplicateStationIds(sections);
        fillVertexes(graph, stationIds);
        fillEdges(sections, graph);
        return new DijkstraShortestPath<>(graph);
    }

    private Set<Long> findNonDuplicateStationIds(List<Section> sections) {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }
        return stationIds;
    }

    private void fillVertexes(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Set<Long> stationIds) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private void fillEdges(List<Section> sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            Long upStationId = section.getUpStationId();
            Long downStationId = section.getDownStationId();

            DefaultWeightedEdge edge = graph.addEdge(upStationId, downStationId);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
