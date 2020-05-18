package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.WrongPathException;

import java.util.ArrayList;
import java.util.List;

public class Edges {
    private final List<Edge> edges;

    public Edges(List<Edge> edges) {
        this.edges = edges;
    }

    public Edges extractPathEdges(List<Long> shortestPath) {
        List<Edge> pathEdges = new ArrayList<>();
        for (int i = 1; i < shortestPath.size(); i++) {
            Long preStationId = shortestPath.get(i - 1);
            Long stationId = shortestPath.get(i);

            pathEdges.add(edges.stream()
                    .filter(edge -> edge.isEdgeOf(preStationId, stationId))
                    .findFirst()
                    .orElseThrow(WrongPathException::new));
        }
        return new Edges(pathEdges);
    }

    public int getDistance() {
        return edges.stream()
                .mapToInt(Edge::getDistance)
                .sum();
    }

    public int getDuration() {
        return edges.stream()
                .mapToInt(Edge::getDuration)
                .sum();
    }
}