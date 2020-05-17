package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import static wooteco.subway.admin.domain.EdgeType.DISTANCE;
import static wooteco.subway.admin.domain.EdgeType.DURATION;

public class Edge extends DefaultWeightedEdge {
    private Long preStationId;
    private Long stationId;
    private int duration;
    private int distance;

    private Edge(Long preStationId, Long stationId, int duration, int distance) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.duration = duration;
        this.distance = distance;
    }

    public static Edge of(LineStation lineStation) {
        return new Edge(lineStation.getPreStationId(),
                lineStation.getStationId(),
                lineStation.getDuration(),
                lineStation.getDistance());
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getValueByType(EdgeType edgeType) {
        if (edgeType.equals(DISTANCE)) {
            return getDistance();
        }
        if (edgeType.equals(DURATION)) {
            return getDuration();
        }
        throw new IllegalArgumentException("올바르지 않은 타입입니다.");
    }
}
