package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscounter {

    INFANT(age -> 0 <= age && age < 6, fare -> 0),
    CHILDREN(age -> 6 <= age && age < 13, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(age -> 13 <= age && age < 19, fare -> (int) ((fare - 350) * 0.8)),
    ORDINAL(age -> age >= 19, fare -> fare)
    ;

    private final Predicate<Integer> condition;
    private final Function<Integer, Integer> function;

    AgeDiscounter(Predicate<Integer> condition, Function<Integer, Integer> function) {
        this.condition = condition;
        this.function = function;
    }

    public static AgeDiscounter from(int age) {
        return Arrays.stream(values())
                .filter(it -> it.condition.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(age + "는 할인할 수 없는 나이입니다."));
    }

    public int discount(int fare) {
        return function.apply(fare);
    }
}
