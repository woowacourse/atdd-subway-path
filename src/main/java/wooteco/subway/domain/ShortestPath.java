package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class ShortestPath {

    private final List<Station> vertexes;
    private final List<Section> sections;
    private final int distance;

    public ShortestPath(List<Station> vertexes, List<Section> sections, int distance) {
        this.vertexes = vertexes;
        this.sections = sections;
        this.distance = distance;
    }

    public List<Station> getVertexes() {
        return vertexes;
    }

    public List<Section> getSections() {
        return sections;
    }

    public int getDistance() {
        return distance;
    }
}
