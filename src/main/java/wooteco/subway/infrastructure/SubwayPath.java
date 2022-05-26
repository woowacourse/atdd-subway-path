package wooteco.subway.infrastructure;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;

public class SubwayPath implements Path {

    private final GraphPath<Station, DefaultWeightedEdge> path;

    public SubwayPath(GraphPath<Station, DefaultWeightedEdge> path) {
        this.path = path;
    }

    @Override
    public List<Station> getVertexList() {
        return path.getVertexList();
    }

    @Override
    public int getWeight() {
        return (int) path.getWeight();
    }
}
