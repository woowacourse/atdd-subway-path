package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum EdgeType {
    DURATION("duration", LineStation::getDuration),
    DISTANCE("distance", LineStation::getDistance);

    private final String edgeType;
    private final Function<LineStation, Integer> expression;

    EdgeType(String edgeType, Function<LineStation, Integer> expression) {
        this.edgeType = edgeType;
        this.expression = expression;
    }

    public static EdgeType of(String type) {
        return Arrays.stream(values())
                .filter(edgeType -> type.equals(edgeType.getEdgeType()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 EdgeType 입니다."));
    }

    public String getEdgeType() {
        return edgeType;
    }

    public int getEdgeValue(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}

