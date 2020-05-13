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

    public boolean containsStationIdAll(final List<Long> stationIds) {
        boolean contain = true;
        for (Long stationId : stationIds) {
            contain = contain && containStationId(stationId);
        }
        return contain;
    }

    private boolean containStationId(Long stationId) {
        return edges.stream()
                .anyMatch(edge -> edge.isSameStationId(stationId));
    }
}
