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

    private WeightedMultigraph<Station, DefaultWeightedEdge> distanceGraph;
    private WeightedMultigraph<Station, DefaultWeightedEdge> durationGraph;

    public Graph(WeightedMultigraph<Station, DefaultWeightedEdge> distanceGraph, WeightedMultigraph<Station, DefaultWeightedEdge> durationGraph) {
        this.distanceGraph = distanceGraph;
        this.durationGraph = durationGraph;
    }

    public static Graph of(Map<Long, Station> stations, List<LineStation> edges) {
        WeightedMultigraph<Station, DefaultWeightedEdge> distanceGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        WeightedMultigraph<Station, DefaultWeightedEdge> durationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations.values()) {
            distanceGraph.addVertex(station);
            durationGraph.addVertex(station);
        }

        for (LineStation edge : edges) {
            if (edge.getPreStationId() == null) {
                continue;
            }
            distanceGraph.setEdgeWeight(distanceGraph.addEdge(stations.get(edge.getPreStationId()), stations.get(edge.getStationId())), edge.getDistance());
            durationGraph.setEdgeWeight(durationGraph.addEdge(stations.get(edge.getPreStationId()), stations.get(edge.getStationId())), edge.getDuration());
        }

        return new Graph(distanceGraph, durationGraph);
    }

    public List<Station> findShortestPath(Station source, Station target, PathType pathType) {
        if (pathType == PathType.DISTANCE) {
            GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(distanceGraph).getPath(source, target);
            if (path == null) {
                return EMPTY_PATH;
            }
            return path.getVertexList();
        }
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(durationGraph).getPath(source, target);
        if (path == null) {
            return EMPTY_PATH;
        }
        return path.getVertexList();
    }

    public int getDuration(Station source, Station target, PathType pathType) {
        List<Station> paths = findShortestPath(source, target, pathType);
        int durationWeight = 0;
        for (int i = 0; i < paths.size()-1; i++) {
            durationWeight += durationGraph.getEdgeWeight(durationGraph.getEdge(paths.get(i), paths.get(i+1)));
        }
        return durationWeight;
    }

    public int getDistance(Station source, Station target, PathType pathType) {
        List<Station> paths = findShortestPath(source, target, pathType);
        int distanceWeight = 0;
        for (int i = 0; i < paths.size()-1; i++) {
            distanceWeight += distanceGraph.getEdgeWeight(distanceGraph.getEdge(paths.get(i), paths.get(i+1)));
        }
        return distanceWeight;
    }
}
