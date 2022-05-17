package wooteco.subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPath {

    private static final int BASIC_COST = 1250;
    private static final int CHARGED_COST = 100;
    private static final int CHARGED_DISTANCE_1 = 10;
    private static final int CHARGED_DISTANCE_2 = 50;
    private static final int CHARGED_UNIT_1 = 5;
    private static final int CHARGED_UNIT_2 = 8;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public ShortestPath(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setVertex(graph, stations);
        setEdgeWeight(graph, sections, stations);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void setVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                               List<Section> sections, List<Station> stations) {
        Map<Long, Station> sectionMap = initSectionMap(stations);

        for (Section section : sections) {
            Station downStation = sectionMap.get(section.getDownStationId());
            Station upStation = sectionMap.get(section.getUpStationId());
            graph.setEdgeWeight(graph.addEdge(downStation, upStation), section.getDistance());
        }
    }

    private Map<Long, Station> initSectionMap(List<Station> stations) {
        Map<Long, Station> sectionMap = new HashMap<>();
        for (Station station : stations) {
            sectionMap.put(station.getId(), station);
        }
        return sectionMap;
    }

    public List<Station> findPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public int calculateDistance(Station source, Station target) {
        List<Station> paths = findPath(source, target);
        int size = paths.size();
        int sum = 0;

        for (int i = 1; i < size; i++) {
            sum += dijkstraShortestPath.getPathWeight(paths.get(i - 1), paths.get(i));
        }
        return sum;
    }

    public int calculateScore(Station source, Station target) {
        int distance = calculateDistance(source, target);
        int cost = BASIC_COST;
        if (distance > CHARGED_DISTANCE_2) {
            cost += calculateCost(distance, CHARGED_DISTANCE_2, CHARGED_UNIT_2);
            distance = CHARGED_DISTANCE_2;
        }
        if (distance > CHARGED_DISTANCE_1) {
            cost += calculateCost(distance, CHARGED_DISTANCE_1, CHARGED_UNIT_1);
        }
        return cost;
    }

    private int calculateCost(int distance, int baseDistance, int unit) {
        return ((distance - baseDistance - 1) / unit + 1) * CHARGED_COST;
    }
}
