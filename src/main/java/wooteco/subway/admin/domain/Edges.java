package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.exception.WrongPathException;

import java.util.*;
import java.util.stream.Collectors;

public class Edges {
    private Set<Edge> edges = new HashSet<>();

    public Edges() {
    }

    public Edges(Set<Edge> edges) {
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
        return new Edges(new HashSet<>(pathEdges));
    }

    public Edges getEdgesExceptFirst() {
        return edges.stream()
                .filter(edge -> Objects.nonNull(edge.getPreStationId()))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Edges::new));
    }

    public void add(Edge edge) {
        edges.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), edge.getPreStationId()))
                .findAny()
                .ifPresent(it -> it.updatePreEdge(edge.getStationId()));

        edges.add(edge);
    }

    public void removeByStationId(Long stationId) {
        Edge targetEdge = edges.stream()
                .filter(it -> Objects.equals(it.getStationId(), stationId))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);

        edges.stream()
                .filter(it -> Objects.equals(it.getPreStationId(), stationId))
                .findFirst()
                .ifPresent(it -> it.updatePreEdge(targetEdge.getPreStationId()));

        edges.remove(targetEdge);
    }

    public List<Long> getSortedStationIds() {
        if (edges.isEmpty()) {
            return new ArrayList<>();
        }

        Edge firstEdge = edges.stream()
                .filter(it -> it.getPreStationId() == null)
                .findFirst()
                .orElseThrow(StationNotFoundException::new);

        List<Long> stationIds = new ArrayList<>();
        stationIds.add(firstEdge.getStationId());

        while (true) {
            Long lastStationId = stationIds.get(stationIds.size() - 1);
            Optional<Edge> nextEdge = edges.stream()
                    .filter(it -> Objects.equals(it.getPreStationId(), lastStationId))
                    .findFirst();

            if (!nextEdge.isPresent()) {
                break;
            }
            stationIds.add(nextEdge.get().getStationId());
        }
        return stationIds;
    }

    public List<Long> getStationIds() {
        return this.edges.stream()
                .map(Edge::getStationId)
                .collect(Collectors.toList());
    }

    public void setAllEdgeWeight(WeightedMultigraph<Long, DefaultWeightedEdge> graph, PathType type) {
        edges.forEach(edge
                -> graph.setEdgeWeight(graph.addEdge(edge.getPreStationId(), edge.getStationId()), type.getWeight(edge)));
    }

    public int size() {
        return edges.size();
    }

    public Set<Edge> getEdges() {
        return edges;
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