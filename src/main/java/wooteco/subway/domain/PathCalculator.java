package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathCalculator {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private PathCalculator(
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathCalculator from(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = getMultiGraph(stations, sections);
        return new PathCalculator(new DijkstraShortestPath<>(graph));
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> getMultiGraph(
            List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            section.setEdgeWeightTo(graph);
        }
        return graph;
    }

    public List<Station> calculateShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public double calculateShortestDistance(Station source, Station target) {
        return dijkstraShortestPath.getPathWeight(source, target);
    }
}
