package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {
    private final Graph<Station, Edge> graph;

    public SubwayGraph() {
        this.graph = new WeightedMultigraph<>(Edge.class);
    }

    public SubwayPath findShortestPath(Station source, Station target) {
        ShortestPathAlgorithm<Station, Edge> shortestPath = new DijkstraShortestPath<>(graph);
        return new SubwayPath(shortestPath.getPath(source, target));
    }

    public void addVertices(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    public void addEdges(List<LineStation> lineStations, Map<Long, Station> stationMapper, PathType pathType) {
        for (LineStation lineStation : lineStations) {
            Station preStation = stationMapper.get(lineStation.getPreStationId());
            Station currentStation = stationMapper.get(lineStation.getStationId());
            Edge edge = new Edge(lineStation, pathType);
            graph.addEdge(preStation, currentStation, edge);
        }
    }
}
