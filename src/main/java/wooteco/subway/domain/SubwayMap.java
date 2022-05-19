package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayMap {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> graph;

    public SubwayMap(final List<Section> sections) {
        graph = initGraph(sections);
    }

    public Path calculatePath(final Station source, final Station target) {
        checkReachable(source, target);
        List<Station> stations = calculatePassingStations(source, target);
        int distance = calculateDistance(source, target);
        return new Path(stations, distance);
    }

    private List<Station> calculatePassingStations(final Station source, final Station target) {
        if (source.equals(target)) {
            return Collections.emptyList();
        }
        return graph.getPath(source, target).getVertexList();
    }

    private int calculateDistance(final Station source, final Station target) {
        return (int) graph.getPath(source, target).getWeight();
    }

    private void checkReachable(final Station source, final Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = this.graph.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException("이동 가능한 경로가 존재하지 않습니다");
        }
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> initGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(graph, sections);
        return new DijkstraShortestPath<>(graph);
    }

    private void initGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                           final List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }
}
