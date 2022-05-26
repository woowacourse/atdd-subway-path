package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum FareAgeStrategy {

    PRESCHOOLER(age -> age < 6 && age >= 0, fare -> 0),
    CHILDREN(age -> age >= 6 && age < 13, fare -> (fare - 350) * 50 / 100),
    TEENAGER(age -> age >= 13 && age < 19, fare -> (fare - 350) * 80 / 100),
    ADULT(age -> age >= 19, fare -> fare);

    private final Function<Integer, Boolean> range;
    private final Function<Integer, Integer> calculate;

    FareAgeStrategy(Function<Integer, Boolean> range, Function<Integer, Integer> calculate) {
        this.range = range;
        this.calculate = calculate;
    }

    public static FareAgeStrategy of(int age) {
        return Arrays.stream(values())
                .filter(it -> it.range.apply(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이가 음수일 수 없습니다."));
    }

    public int calculate(int fare) {
        return calculate.apply(fare);
    }
}
