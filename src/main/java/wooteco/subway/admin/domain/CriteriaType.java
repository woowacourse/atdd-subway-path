package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum CriteriaType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private final Function<LineStation, Integer> expression;

    CriteriaType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }

    public static CriteriaType of(String input) {
        return Arrays.stream(values())
            .filter(instance -> instance.name().equalsIgnoreCase(input))
            .findFirst()
            .orElseThrow(AssertionError::new);
    }

    public int get(LineStation it) {
        return this.expression.apply(it);
    }
}
