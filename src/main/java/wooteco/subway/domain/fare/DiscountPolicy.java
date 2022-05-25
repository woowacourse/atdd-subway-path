package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountPolicy {

    FREE(age -> age < 6 || age >= 65, fare -> 0),
    CHILD(age -> age >= 6 && age < 13, fare -> (fare - 350) * 50 / 100),
    TEENAGER(age -> age >= 13 && age < 19, fare -> (fare - 350) * 80 / 100),
    ADULT(age -> age >= 19 && age < 65, fare -> fare);

    private static final String NOT_EXIST_MATCHED_AGE = "일치하는 나이가 없습니다.";

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    DiscountPolicy(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int calculate(int age, int fare) {
        return Arrays.stream(values())
                .filter(policy -> policy.predicate.test(age))
                .map(policy -> policy.function.apply(fare))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MATCHED_AGE));
    }
}
