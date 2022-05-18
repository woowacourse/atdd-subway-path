package wooteco.subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathInfo {

    private final DijkstraShortestPath dijkstraShortestPath;

    public PathInfo(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        stations.forEach(graph::addVertex);

        Map<Long, Station> sectionMap = new HashMap<>();
        for(Station station : stations) {
            sectionMap.put(station.getId(), station);
        }

        for(Section section : sections) {
            Station downStation = sectionMap.get(section.getDownStationId());
            Station upStation = sectionMap.get(section.getUpStationId());
            graph.setEdgeWeight(graph.addEdge(downStation,upStation), section.getDistance());
        }
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public List<Station> findPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public int calculateMinDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }

    public int calculateScore(Station source, Station target) {
        int distance = calculateMinDistance(source, target);
        int cost = 1250;
        if (distance > 50) {
            cost += calculateCost(distance, 50, 8);
            distance = 50;
        }
        if (distance > 10) {
            cost += calculateCost(distance, 10, 5);
        }
        return cost;
    }

    private int calculateCost(int distance, int baseDistance, int unit) {
        return ((distance - baseDistance -1 ) / unit + 1) * 100;
    }
}
