package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Edges {
    private final Set<Edge> edges;

    public Edges(final Set<Edge> edges) {
        this.edges = edges;
    }

    public List<Long> getStationIds() {
        return edges.stream()
                .map(Edge::getStationId)
                .collect(Collectors.toList());
    }
}
