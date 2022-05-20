package wooteco.subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public ShortestPath(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        setVertex(graph, stations);
        setEdgeWeight(graph, sections, stations);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void setVertex(WeightedMultigraph<Station, SectionEdge> graph, List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph,
                               List<Section> sections, List<Station> stations) {
        Map<Long, Station> sectionMap = initSectionMap(stations);

        for (Section section : sections) {
            Station downStation = sectionMap.get(section.getDownStationId());
            Station upStation = sectionMap.get(section.getUpStationId());
            SectionEdge sectionEdge = new SectionEdge(section.getLineId(), section.getDistance());

            graph.addEdge(downStation, upStation, sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
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
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
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

    public List<Long> findLineIds(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getEdgeList()
                .stream()
                .map(SectionEdge::getLineId)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    private static class SectionEdge extends DefaultWeightedEdge {

        private final Long lineId;
        private final int distance;

        public SectionEdge(Long lineId, int distance) {
            this.lineId = lineId;
            this.distance = distance;
        }

        public Long getLineId() {
            return lineId;
        }

        public int getDistance() {
            return distance;
        }
    }
}
