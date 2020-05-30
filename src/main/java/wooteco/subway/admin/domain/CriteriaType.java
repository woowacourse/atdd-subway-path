package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum CriteriaType {
    DISTANCE("distance", LineStation::getDistance),
    DURATION("duration", LineStation::getDuration);

    private final String name;
    private final Function<LineStation, Integer> expression;

    CriteriaType(String name,
        Function<LineStation, Integer> expression) {
        this.name = name;
        this.expression = expression;
    }

    public static CriteriaType of(String input) {
        return Arrays.stream(values())
            .filter(instance -> instance.name.equalsIgnoreCase(input))
            .findFirst()
            .orElseThrow(AssertionError::new);
    }

    public int get(LineStation it) {
        return this.expression.apply(it);
    }
}
