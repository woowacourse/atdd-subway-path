package wooteco.subway.admin.service;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.exception.CanNotCreateGraphException;
import wooteco.subway.admin.exception.LineNotConnectedException;

import java.util.List;
import java.util.Set;

@Service
public class GraphService {
    public static final String DISTANCE = "DISTANCE";
    public static final String DURATION = "DURATION";

    public List<Long> calculateshortestPathIDs(LineStations lineStations, Station sourceStation, Station targetStation, PathType type) {

        ShortestPathAlgorithm<Long, DefaultEdge> shortestPathAlgorithm = prepareAlgorithm(type, lineStations);
        List<Long> shortestPathIds;
        try {
            shortestPathIds = shortestPathAlgorithm.getPath(sourceStation.getId(), targetStation.getId()).getVertexList();
        } catch (NullPointerException e) {
            throw new LineNotConnectedException();
        }
        return shortestPathIds;
    }

    private ShortestPathAlgorithm<Long, DefaultEdge> prepareAlgorithm(PathType type, LineStations lineStations) {
        WeightedGraph<Long, DefaultEdge> graph;

        try {
            graph = initGraph(lineStations, type);
        } catch (IllegalArgumentException e) {
            throw new CanNotCreateGraphException();
        }

        return new DijkstraShortestPath<>(graph);
    }

    private WeightedGraph<Long, DefaultEdge> initGraph(LineStations lineStations, PathType type) {
        WeightedGraph<Long, DefaultEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        Set<Long> allStationIds = lineStations.getAllLineStationId();

        for (Long stationId : allStationIds) {
            graph.addVertex(stationId);
        }

        for (LineStation lineStation : lineStations.getLineStations()) {
            if (lineStation.getPreStationId() != null) {
                DefaultEdge edge = graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId());
                graph.setEdgeWeight(edge, type.findWeightOf(lineStation));
            }
        }
        return graph;
    }
}
