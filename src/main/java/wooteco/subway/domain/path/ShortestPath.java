package wooteco.subway.domain.path;

import java.util.List;
import org.jgrapht.GraphPath;
import wooteco.subway.domain.LineWeightedEdge;
import wooteco.subway.domain.Station;

public class ShortestPath {

    private final GraphPath<Station, LineWeightedEdge> path;

    public ShortestPath(GraphPath<Station, LineWeightedEdge> path) {
        this.path = path;
        validateRoute(path);
    }

    public List<Station> getShortestRoute() {
        return path.getVertexList();
    }

    public int getShortestDistance() {
        return (int) path.getWeight();
    }

    private void validateRoute(GraphPath<Station, LineWeightedEdge> route) {
        if (route == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }

}
