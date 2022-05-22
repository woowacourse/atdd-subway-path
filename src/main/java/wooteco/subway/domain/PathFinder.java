package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    final private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(final List<Station> stations, final List<Section> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(stations, graph);
        addEdge(sections, graph);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addEdge(final List<Section> sections, final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (final Section section : sections) {
            final DefaultWeightedEdge upToDownEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
            final DefaultWeightedEdge downToUpEdge = graph.addEdge(section.getDownStation(), section.getUpStation());
            graph.setEdgeWeight(upToDownEdge, section.getDistance());
            graph.setEdgeWeight(downToUpEdge, section.getDistance());
        }
    }

    private void addVertex(final List<Station> stations, final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (final Station station : stations) {
            graph.addVertex(station);
        }
    }

    public Path findPath(final Station source, final Station target) {
        validateSameStation(source, target);
        final GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        validateGraphPath(graphPath);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void validateGraphPath(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (null == graphPath) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }
    }

    private void validateSameStation(final Station source, final Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }
    }
}
