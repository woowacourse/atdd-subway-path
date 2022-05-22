package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setVertex(stations, graph);
        setEdgeWeight(sections, graph);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void setVertex(List<Station> stations, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (var station : stations) {
            graph.addVertex(station.getId());
        }
    }

    private void setEdgeWeight(List<Section> sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (var section : sections) {
            var upStationId = section.getUpStationId();
            var downStationId = section.getDownStationId();
            var distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), distance);
        }
    }

    public GraphPath<Long, DefaultWeightedEdge> getPath(long source, long target) {
        return dijkstraShortestPath.getPath(source, target);
    }
}
