package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPath {

    private final List<Station> vertexes;
    private final int distance;

    public ShortestPath(GraphPath<Station, DefaultWeightedEdge> path) {
        validatePathExist(path);
        this.vertexes = path.getVertexList();
        this.distance = (int) path.getWeight();
    }

    private void validatePathExist(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalStateException("해당 경로가 존재하지 않습니다.");
        }
    }

    public List<Station> getVertexes() {
        return vertexes;
    }

    public int getDistance() {
        return distance;
    }
}
