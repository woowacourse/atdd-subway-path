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
        List<Station> paths = findPath(source, target);
        int size = paths.size();
        int sum = 0;

        for (int i = 0; i<size - 1; ++i) {
            sum += dijkstraShortestPath.getPathWeight(paths.get(i), paths.get(i+1));
        }
        return sum;
    }
}
