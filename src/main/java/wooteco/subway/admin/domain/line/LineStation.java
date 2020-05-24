package wooteco.subway.admin.domain.line;

import java.util.Objects;

import org.jgrapht.WeightedGraph;

import wooteco.subway.admin.domain.line.path.EdgeWeightStrategy;
import wooteco.subway.admin.domain.line.path.RouteEdge;

public class LineStation {
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    public LineStation(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
    }

    public void addEdgeTo(WeightedGraph<Long, RouteEdge> graph, EdgeWeightStrategy edgeWeightStrategy) {
        if (Objects.nonNull(preStationId)) {
            RouteEdge edge = new RouteEdge(distance, duration);
            graph.addEdge(preStationId, stationId, edge);
            edgeWeightStrategy.setWeight(graph, edge);
        }
    }
}
