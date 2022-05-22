package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathSearcher {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private PathSearcher(
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathSearcher from(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = getMultiGraph(stations, sections);
        return new PathSearcher(new DijkstraShortestPath<>(graph));
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

    public List<Station> searchShortestPath(Station source, Station target) {
        List<Station> vertexList = dijkstraShortestPath.getPath(source, target).getVertexList();
        return List.copyOf(vertexList);
    }

    public Distance calculateShortestDistance(Station source, Station target) {
        return Distance.fromKilometer(dijkstraShortestPath.getPathWeight(source, target));
    }
}
