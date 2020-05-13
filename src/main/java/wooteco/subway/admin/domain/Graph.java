package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {
    public static final ArrayList<Station> EMPTY_PATH = new ArrayList<>();

    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private Graph(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static Graph of(Map<Long, Station> stations, List<LineStation> edges) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : stations.values()) {
            graph.addVertex(station);
        }

        for (LineStation edge : edges) {
            if (edge.getPreStationId() == null) {
                continue;
            }
            graph.setEdgeWeight(graph.addEdge(stations.get(edge.getPreStationId()), stations.get(edge.getStationId())), edge.getDistance());
        }


        return new Graph(new DijkstraShortestPath<>(graph));
    }

    public List<Station> findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            return EMPTY_PATH;
        }
        return path.getVertexList();
    }

}
