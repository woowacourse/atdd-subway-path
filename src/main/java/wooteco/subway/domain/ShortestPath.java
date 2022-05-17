package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPath {

    private final List<Station> vertexes;
    private final double weight;

    public ShortestPath(GraphPath<Station, DefaultWeightedEdge> path) {
        this.vertexes = path.getVertexList();
        this.weight = path.getWeight();
    }

    public List<Station> getVertexes() {
        return vertexes;
    }

    public double getWeight() {
        return weight;
    }
}
