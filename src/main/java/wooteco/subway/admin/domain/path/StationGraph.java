package wooteco.subway.admin.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.PathSearchType;

import java.util.Set;

// 모든 Station을 가중치 있는 간선으로 잇는 그래프를 생성하는 역할
public class StationGraph extends WeightedMultigraph<Long, DefaultWeightedEdge> {
    public StationGraph(Class<? extends DefaultWeightedEdge> edgeClass) {
        super(edgeClass);
    }

    public static StationGraph of(LineStations lineStations, PathSearchType weightType) {
        StationGraph graph = new StationGraph(DefaultWeightedEdge.class);
        Set<Long> allStationIds = lineStations.getAllLineStationId();

        addAllVertex(graph, allStationIds);

        setAllEdgeWeight(lineStations, weightType, graph);
        return graph;
    }

    private static void setAllEdgeWeight(LineStations lineStations, PathSearchType weightType, StationGraph graph) {
        for (LineStation lineStation : lineStations.getLineStations()) {
            if (lineStation.isFirstStation()) {
                continue;
            }
            DefaultWeightedEdge edge = graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId());
            graph.setEdgeWeight(edge, weightType.getValueByPathSearchType(lineStation));
        }
    }

    private static void addAllVertex(StationGraph graph, Set<Long> allStationIds) {
        for (Long stationId : allStationIds) {
            graph.addVertex(stationId);
        }
    }
}
