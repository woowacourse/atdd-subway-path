package wooteco.subway.admin.domain.path;

import java.util.List;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.domain.LineStation;

public class ShortestPath {

    DijkstraShortestPath<Long, WeightedEdge> path;

    private ShortestPath(DijkstraShortestPath<Long, WeightedEdge> path) {
        this.path = path;
    }

    public static ShortestPath createDistancePath(List<LineStation> lineStations) {
        WeightedGraph<Long, WeightedEdge> graph
            = new WeightedMultigraph<>(WeightedEdge.class);

        for (LineStation station : lineStations) {
            graph.addVertex(station.getStationId());
            if (station.isFirst()) {
                continue;
            }
            WeightedEdge weightedEdge
                = graph.addEdge(station.getPreStationId(), station.getStationId());
            weightedEdge.setSubWeight(station.getDuration());

            graph.setEdgeWeight(weightedEdge, station.getDistance());
        }

        DijkstraShortestPath<Long, WeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return new ShortestPath(shortestPath);
    }

    public DijkstraShortestPath<Long, WeightedEdge> getPath() {
        return path;
    }

    public List<Long> getVertexList(Long source, Long target) {
        return path.getPath(source, target).getVertexList();
    }

    public int getWeight(Long source, Long target) {
        return (int) path.getPath(source, target).getWeight();
    }

    public int getSubWeight(Long source, Long target) {
        return path.getPath(source, target)
            .getEdgeList()
            .stream()
            .mapToInt(WeightedEdge::getSubWeight)
            .sum();
    }
}
