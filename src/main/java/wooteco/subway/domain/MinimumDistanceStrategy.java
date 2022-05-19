package wooteco.subway.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class MinimumDistanceStrategy implements PathStrategy {

    @Override
    public Path findPath(List<Station> stations, List<Section> sections, Station from, Station to) {
        GraphPath<Station, Section> graphPath = getGraphPath(stations, sections, from, to);
        return new Path(graphPath.getVertexList(), graphPath.getEdgeList(), (int) graphPath.getWeight());
    }

    private GraphPath<Station, Section> getGraphPath(List<Station> stations,
                                                       List<Section> sections, Station from, Station to) {
        WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        stations.forEach(graph::addVertex);

        Map<Long, Station> sectionMap = stations.stream()
                .collect(Collectors.toMap(Station::getId, it -> it));

        for (Section section : sections) {
            Station downStation = sectionMap.get(section.getDownStationId());
            Station upStation = sectionMap.get(section.getUpStationId());
            graph.addEdge(downStation, upStation, section);
            graph.setEdgeWeight(section, section.getDistance());
        }
        return new DijkstraShortestPath<>(graph).getPath(from, to);
    }
}
