package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestPath {
    DijkstraShortestPath path;

    private ShortestPath(DijkstraShortestPath path) {
        this.path = path;
    }

    public static ShortestPath createDistancePath(List<LineStation> lineStations) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (LineStation station : lineStations) {
            graph.addVertex(station.getStationId());
            if (station.isFirst()) {
                continue;
            }
            graph.setEdgeWeight(
                    graph.addEdge(station.getPreStationId(), station.getStationId()),
                    station.getDistance()
            );
        }

        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        return new ShortestPath(shortestPath);
    }

    public DijkstraShortestPath getPath() {
        return path;
    }

    public List<Long> getVertexList(Long sourceId, Long targetId) {
        return path.getPath(sourceId, targetId).getVertexList();
    }

    public int getWeight(Long sourceId, Long targetId) {
        return (int) path.getPath(sourceId, targetId).getWeight();
    }
}
