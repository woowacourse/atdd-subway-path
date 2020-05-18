package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;

import java.util.List;

public class ShortestPath {
    private GraphPath<Station, LineStationEdge> path;

    private ShortestPath(GraphPath<Station, LineStationEdge> path) {
        this.path = path;
    }

    public static ShortestPath of(Graph graph, Station source, Station target) {
        GraphPath<Station, LineStationEdge> shortestPath = graph.getShortestPath(source, target);
        return new ShortestPath(shortestPath);
    }

    public List<Station> findShortestPath() {
        return path.getVertexList();
    }

    public int getTotalDuration() {
        return getTotalValueFrom(PathType.DURATION);
    }

    public int getTotalDistance() {
        return getTotalValueFrom(PathType.DISTANCE);
    }

    private int getTotalValueFrom(PathType pathType) {
        return path.getEdgeList()
                .stream()
                .map(edge -> edge.getValueOf(pathType))
                .reduce(0, Integer::sum);
    }
}
