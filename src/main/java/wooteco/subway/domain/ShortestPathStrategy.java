package wooteco.subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPathStrategy implements PathStrategy {

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public ShortestPathStrategy(List<Station> stations, List<Section> sections) {
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
