package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum DijkstraEdgeWeightType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> expression;

    DijkstraEdgeWeightType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }

    public static DijkstraEdgeWeightType of(final String typeName) {
        return valueOf(typeName.toUpperCase());
    }

    public double getEdgeWeight(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}
