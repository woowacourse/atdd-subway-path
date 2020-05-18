package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph implements SubwayMap {
    DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;
    private WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public SubwayGraph(WeightedMultigraph<Long, DefaultWeightedEdge> graph, DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath) {
        this.graph = graph;
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static SubwayGraph makeGraph(PathType type, Stations stations, LineStations lineStations) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations.getStations()) {
            graph.addVertex(station.getId());
        }

        for (LineStation station : lineStations.getLineStations()) {
            graph.setEdgeWeight(graph.addEdge(station.getPreStationId(), station.getStationId()), type.getGetWeight(station));
        }

        return new SubwayGraph(graph, new DijkstraShortestPath<>(graph));
    }

    @Override
    public List<Long> findShortestPath(Long source, Long target) {
        List<Long> shortestPath;
        try {
            shortestPath = dijkstraShortestPath.getPath(source, target)
                    .getVertexList();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다. 노선도를 확인해주세요.");
        }
        return shortestPath;
    }

    @Override
    public int getPathWeight(Long sourceId, Long targetId) {
        return (int) dijkstraShortestPath.getPathWeight(sourceId, targetId);
    }
}
