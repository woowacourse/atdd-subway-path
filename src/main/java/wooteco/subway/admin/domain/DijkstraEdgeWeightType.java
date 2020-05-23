package wooteco.subway.admin.domain;

public enum DijkstraEdgeWeightType {
    DISTANCE,
    DURATION;

    public static DijkstraEdgeWeightType of(final String typeName) {
        return valueOf(typeName.toUpperCase());
    }

    public double getWeight(LineStation lineStation) {
        return this == DISTANCE ? lineStation.getDistance() : lineStation.getDuration();
    }
}
