package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.PathNotLinkedException;
import wooteco.subway.line.dao.SectionTable;

import java.util.List;

public class PathFinder {
    private final List<SectionTable> sectionTables;
    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<SectionTable> sectionTables) {
        this.sectionTables = sectionTables;
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setGraph();
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void setGraph() {
        for (SectionTable sectionTable : sectionTables) {
            Long upStationId = sectionTable.getUpStationId();
            Long downStationId = sectionTable.getDownStationId();
            graph.addVertex(upStationId);
            graph.addVertex(downStationId);
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), sectionTable.getDistance());
        }
    }

    public List<Long> getShortestStations(Long sourceId, Long targetId) {
        List<Long> stationIds;
        try {
            stationIds = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new PathNotLinkedException();
        }

        return stationIds;
    }

    public int getShortestDistance(Long sourceId, Long targetId) {
        double pathWeight = dijkstraShortestPath.getPath(sourceId, targetId).getWeight();
        return (int) pathWeight;
    }
}
