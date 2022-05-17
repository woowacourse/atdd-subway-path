package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static PathFinder createPathFinder(Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createGraph(sections);
        return new PathFinder(graph);
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(Sections sections) {
        WeightedMultigraph<Station , DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Station station : sections.getDistinctStations()) {
            graph.addVertex(station);
        }
        for (Section value : sections.getValues()) {
            graph.setEdgeWeight(graph.addEdge(value.getUpStation(), value.getDownStation()), value.getDistance());
        }

        return graph;
    }

    public List<Station> findShortestPath(Station source, Station target) {
        validateSameStation(source, target);

        return new DijkstraShortestPath(graph)
                .getPath(source, target)
                .getVertexList();
    }

    public double findShortestPathDistance(Station source, Station target) {
        validateSameStation(source, target);

        return new DijkstraShortestPath(graph)
                .getPath(source, target)
                .getWeight();
    }

    private void validateSameStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }
}
