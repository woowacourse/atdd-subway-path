package wooteco.subway.admin.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private WeightedMultigraph<Long, DefaultWeightedEdge> distanceGraph;
    private WeightedMultigraph<Long, DefaultWeightedEdge> durationGraph;
    private DijkstraShortestPath<Long, Integer> dijkstraShortestPath;
    private List<Line> lines;

    public Path(List<Line> lines) {
        this.lines = lines;
        distanceGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        durationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        init();
    }

    private void init() {
        for (Line line : lines) {
            List<LineStation> stations = line.getLineStations();
            setVertexesAndEdges(stations);
        }
    }

    private void setVertexesAndEdges(List<LineStation> stations) {
        for (LineStation lineStation : stations) {
            distanceGraph.addVertex(lineStation.getStationId());
            durationGraph.addVertex(lineStation.getStationId());
            if (lineStation.isFirstLineStation()) {
                continue;
            }
            distanceGraph.setEdgeWeight(distanceGraph.addEdge(lineStation.getPreStationId(),
                lineStation.getStationId()), lineStation.getDistance());
            durationGraph.setEdgeWeight(durationGraph.addEdge(lineStation.getPreStationId(),
                lineStation.getStationId()), lineStation.getDuration());
        }
    }

    public List<Long> searchShortestPath(Station source, Station target, String type) {
        if ("DISTANCE".equals(type)) {
            dijkstraShortestPath = new DijkstraShortestPath(distanceGraph);
        }
        if ("DURATION".equals(type)) {
            dijkstraShortestPath = new DijkstraShortestPath(durationGraph);
        }
        return dijkstraShortestPath.getPath(source.getId(), target.getId()).getVertexList();
    }

    public int calculateDistance(List<Long> stationIds) {
        int sum = 0;
        for (int i = 0; i < stationIds.size() - 1; i++) {
            sum += distanceGraph.getEdgeWeight(
                distanceGraph.getEdge(stationIds.get(i), stationIds.get(i + 1)));
        }
        return sum;
    }

    public int calculateDuration(List<Long> stationIds) {
        int sum = 0;
        for (int i = 0; i < stationIds.size() - 1; i++) {
            sum += durationGraph.getEdgeWeight(
                durationGraph.getEdge(stationIds.get(i), stationIds.get(i + 1)));
        }
        return sum;
    }
}
