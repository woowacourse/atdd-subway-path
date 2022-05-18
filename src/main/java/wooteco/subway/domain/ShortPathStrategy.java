package wooteco.subway.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortPathStrategy implements PathStrategy {

    @Override
    public List<Station> findPath(List<Station> stations, List<Section> sections, Station from, Station to) {
        return buildSubwayMap(stations, sections).getPath(from, to).getVertexList();
    }

    @Override
    public List<Section> findSections(List<Station> stations, List<Section> sections, Station from, Station to) {
        return buildSubwayMap(stations, sections).getPath(from, to).getEdgeList();
    }

    @Override
    public int calculateDistance(List<Station> stations, List<Section> sections, Station from, Station to) {
        return (int) buildSubwayMap(stations, sections).getPath(from, to).getWeight();
    }

    private DijkstraShortestPath buildSubwayMap(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph(Section.class);
        stations.forEach(graph::addVertex);

        Map<Long, Station> sectionMap = stations.stream()
                .collect(Collectors.toMap(Station::getId, it -> it));

        for(Section section : sections) {
            Station downStation = sectionMap.get(section.getDownStationId());
            Station upStation = sectionMap.get(section.getUpStationId());
            graph.addEdge(downStation, upStation, section);
            graph.setEdgeWeight(section, section.getDistance());
        }
        return new DijkstraShortestPath(graph);
    }
}
