package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {
    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;
    private PathType pathType;

    private PathEdge(Long preStationId, Long stationId, int distance, int duration,
        PathType pathType) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
        this.pathType = pathType;
    }

    public static PathEdge of(LineStation lineStation, PathType pathType) {
        return new PathEdge(lineStation.getPreStationId(), lineStation.getStationId(),
            lineStation.getDistance(),
            lineStation.getDuration(), pathType);
    }

    @Override
    protected double getWeight() {
        return pathType.getWeight(this);
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

    public PathType getPathType() {
        return pathType;
    }
}
