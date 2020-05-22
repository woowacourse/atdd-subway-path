package wooteco.subway.admin.domain;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Long> vertices;
    private final List<Edge> edges;

    public Path(List<Long> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<Long> vertices() {
        return Collections.unmodifiableList(vertices);
    }

    public int distance() {
        return edges.stream()
                .mapToInt(Edge::getDistance)
                .sum();
    }

    public int duration() {
        return edges.stream()
                .mapToInt(Edge::getDuration)
                .sum();
    }
}
