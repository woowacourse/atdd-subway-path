package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;

import java.util.List;

@Service
public class GraphService {

    public WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(PathType type, List<Station> stations, List<LineStation> lineStations) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
        for (LineStation station : lineStations) {
            graph.setEdgeWeight(graph.addEdge(station.getPreStationId(), station.getStationId()), type.getWeight(station));
        }

        return graph;
    }

    public List<Long> findShortestPath(Long source, Long target, DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath) {
        List<Long> shortestPath;
        try {
            shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다. 노선도를 확인해주세요.");
        }
        return shortestPath;
    }
}
