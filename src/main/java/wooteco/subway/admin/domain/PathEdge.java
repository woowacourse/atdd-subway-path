package wooteco.subway.admin.domain;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {
    private final Long preStationId;
    private final Long stationId;
    private final Integer distance;
    private final Integer duration;
    private final PathCriteria criteria;

    public PathEdge(final LineStation lineStation, PathCriteria criteria) {
        preStationId = lineStation.getPreStationId();
        stationId = lineStation.getStationId();
        distance = lineStation.getDistance();
        duration = lineStation.getDuration();
        this.criteria = criteria;
    }

    public boolean isNotFirst() {
        return Objects.nonNull(preStationId);
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

    @Override
    public double getWeight() {
        if (criteria == PathCriteria.DISTANCE) {
            return distance;
        }
        return duration;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final PathEdge that = (PathEdge)o;
        return Objects.equals(preStationId, that.preStationId) &&
            Objects.equals(stationId, that.stationId) &&
            Objects.equals(distance, that.distance) &&
            Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preStationId, stationId, distance, duration);
    }
}
