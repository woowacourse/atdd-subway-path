package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.path.SubwayMapException;
import wooteco.subway.section.domain.Section;

import java.util.List;

public class SubwayMap {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> subwayMap;

    public SubwayMap(List<Section> sections) {
        this.subwayMap = drawSubwayMap(sections);
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> drawSubwayMap(List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> weightedMultiGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> addSection(weightedMultiGraph, section));
        return new DijkstraShortestPath<>(weightedMultiGraph);
    }

    private void addSection(WeightedMultigraph<Long, DefaultWeightedEdge> weightedMultiGraph, Section section) {
        Long upStationId = section.getUpStation().getId();
        Long downStationId = section.getDownStation().getId();
        weightedMultiGraph.addVertex(upStationId);
        weightedMultiGraph.addVertex(downStationId);
        DefaultWeightedEdge defaultWeightedEdge = weightedMultiGraph.addEdge(upStationId, downStationId);
        weightedMultiGraph.setEdgeWeight(defaultWeightedEdge, section.getDistance());
    }

    public List<Long> findShortestPath(long sourceStationId, long targetStationId) {
        return findPath(sourceStationId, targetStationId)
                .getVertexList();
    }

    private GraphPath<Long, DefaultWeightedEdge> findPath(long sourceStationId, long targetStationId) {
        try {
            return subwayMap.getPath(sourceStationId, targetStationId);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new SubwayMapException();
        }
    }

    public int findShortestDistance(long sourceStationId, long targetStationId) {
        return (int) findPath(sourceStationId, targetStationId)
                .getWeight();
    }
}
