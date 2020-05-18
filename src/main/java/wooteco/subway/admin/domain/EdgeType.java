package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum EdgeType {
    DURATION(LineStation::getDuration),
    DISTANCE(LineStation::getDistance);

    private final Function<LineStation, Integer> expression;

    EdgeType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }

    public static EdgeType of(String type) {
        String upperCaseType = type.toUpperCase();
        return Arrays.stream(values())
                .filter(edgeType -> upperCaseType.equals(edgeType.name()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 EdgeType 입니다."));
    }

    public int getEdgeValue(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}

