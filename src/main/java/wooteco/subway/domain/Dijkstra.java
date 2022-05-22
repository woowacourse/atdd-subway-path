package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dijkstra implements PathFactory {

    private final DijkstraShortestPath<Long, ShortestPathEdge> dijkstraShortestPath;

    public Dijkstra(Sections sections) {
        WeightedMultigraph<Long, ShortestPathEdge> graph = new WeightedMultigraph(ShortestPathEdge.class);
        initVertexes(graph, sections);
        addEdgeWeights(graph, sections);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void initVertexes(WeightedMultigraph<Long, ShortestPathEdge> graph, Sections sections) {
        for (Long stationId : sections.getStationIdsInSections()) {
            graph.addVertex(stationId);
        }
    }

    private void addEdgeWeights(WeightedMultigraph<Long, ShortestPathEdge> graph, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.addEdge(section.getUpStationId(), section.getDownStationId(),
                    new ShortestPathEdge(section.getLineId(), section.getDistance()));
        }
    }

    @Override
    public int findShortestDistance(Long source, Long target) {
        try {
            double weight = dijkstraShortestPath.getPath(source, target).getWeight();
            return (int) weight;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("구간에 없는 역은 출발지 또는 목적지로 선택할 수 없습니다.");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
    }

    @Override
    public List<Long> findShortestPath(Long source, Long target) {
        try {
            return dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("구간에 없는 역은 출발지 또는 목적지로 선택할 수 없습니다.");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
    }

    @Override
    public Set<Long> findLines(Long source, Long target) {
        final Set<Long> lineIds = new HashSet<>();
        for (ShortestPathEdge shortestPathEdge : dijkstraShortestPath.getPath(source, target).getEdgeList()) {
            lineIds.add(shortestPathEdge.getLineId());
        }
        return lineIds;
    }
}
