package wooteco.subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathInfo {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathInfo(List<Station> stations, List<Section> sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
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
    }

    public List<Station> findPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
