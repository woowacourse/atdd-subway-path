package wooteco.subway.admin.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import wooteco.subway.admin.domain.LineStation;

import java.util.List;
import java.util.Objects;

public class ShortestPath {
    private DijkstraShortestPath<Long, WeightedEdge> path;
    private PathType pathType;

    public ShortestPath(DijkstraShortestPath<Long, WeightedEdge> path, PathType pathType) {
        this.path = path;
        this.pathType = pathType;
    }

    public static ShortestPath of(List<LineStation> lineStations, PathType pathType) {
        WeightedGraph<Long, WeightedEdge> graph
                = new DirectedWeightedMultigraph<>(WeightedEdge.class);

        for (LineStation station : lineStations) {
            graph.addVertex(station.getStationId());
            if (station.isFirst()) {
                continue;
            }
            WeightedEdge weightedEdge
                    = graph.addEdge(station.getPreStationId(), station.getStationId());
            weightedEdge.setSubWeight(pathType.findSubWeight(station));
            graph.setEdgeWeight(weightedEdge, pathType.findWeight(station));
        }

        DijkstraShortestPath<Long, WeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return new ShortestPath(shortestPath, pathType);
    }

    public DijkstraShortestPath<Long, WeightedEdge> getTotalPath() {
        return path;
    }

    public GraphPath<Long, WeightedEdge> getPath(Long source, Long target) {
        try {
            GraphPath<Long, WeightedEdge> result = path.getPath(source, target);
            if (Objects.isNull(result)) {
                throw new IllegalArgumentException();
            }
            return result;
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 경로입니다.");
        }
    }

    public List<Long> getVertexList(Long source, Long target) {
        return getPath(source, target).getVertexList();
    }

    private int getWeight(Long source, Long target) {
        return (int) getPath(source, target).getWeight();
    }

    private int getSubWeight(Long source, Long target) {
        return getPath(source, target)
                .getEdgeList()
                .stream()
                .mapToInt(WeightedEdge::getSubWeight)
                .sum();
    }

    public int getDistance(Long source, Long target) {
        if (pathType.equals(PathType.DISTANCE)) {
            return getWeight(source, target);
        }
        return getSubWeight(source, target);
    }

    public int getDuration(Long source, Long target) {
        if (pathType.equals(PathType.DURATION)) {
            return getWeight(source, target);
        }
        return getSubWeight(source, target);
    }
}
