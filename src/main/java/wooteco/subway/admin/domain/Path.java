package wooteco.subway.admin.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private WeightedMultigraph<Station, DefaultWeightedEdge> distanceGraph;
    private WeightedMultigraph<Station, DefaultWeightedEdge> durationGraph;
    private DijkstraShortestPath<Station, Integer> dijkstraShortestPath;
    private List<Station> path;

    public Path() {
        distanceGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        durationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public void addStation(Station station) {
        distanceGraph.addVertex(station);
        durationGraph.addVertex(station);
    }

    public void deleteStation(Station station) {
        distanceGraph.removeVertex(station);
        durationGraph.removeVertex(station);
    }

    public void setDistanceWeight(Station preStation, Station station, int distance) {
        distanceGraph.setEdgeWeight(distanceGraph.addEdge(preStation, station), distance);
    }

    public void removeDistanceEdge(Station preStation, Station station) {
        distanceGraph.removeEdge(preStation, station);
    }

    public void setDurationWeight(Station preStation, Station station, int duration) {
        durationGraph.setEdgeWeight(durationGraph.addEdge(preStation, station), duration);
    }

    public void removeDurationEdge(Station preStation, Station station) {
        durationGraph.removeEdge(preStation, station);
    }

    public List<Station> searchShortestDistancePath(Station source, Station target) {
        dijkstraShortestPath = new DijkstraShortestPath(distanceGraph);
        return searchShortestPath(source, target);
    }

    public List<Station> searchShortestDurationPath(Station source, Station target) {
        dijkstraShortestPath = new DijkstraShortestPath(durationGraph);
        return searchShortestPath(source, target);
    }

    private List<Station> searchShortestPath(Station source, Station target) {
        path = dijkstraShortestPath.getPath(source, target).getVertexList();
        return path;
    }

    public int calculateDistance() {
        int sum = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            sum += distanceGraph.getEdgeWeight(distanceGraph.getEdge(path.get(i), path.get(i + 1)));
        }
        return sum;
    }

    public int calculateDuration() {
        int sum = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            sum += durationGraph.getEdgeWeight(durationGraph.getEdge(path.get(i), path.get(i + 1)));
        }
        return sum;
    }
}
