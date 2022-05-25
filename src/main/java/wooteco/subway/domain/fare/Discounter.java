package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Discounter {

    INFANT(age -> age < 6, fare -> 0.0),
    CHILDREN(age -> age >= 6 && age < 13, fare -> (fare - 350) * 0.5),
    TEENAGER(age -> age >= 13 && age < 19, fare -> (fare - 350) * 0.8),
    ADULT(age -> age >= 19, fare -> fare)
    ;

    private final Predicate<Integer> condition;
    private final Function<Double, Double> discount;

    Discounter(final Predicate<Integer> condition, final Function<Double, Double> discount) {
        this.condition = condition;
        this.discount = discount;
    }

    public static double discount(final double fare, final int age) {
        return Arrays.stream(Discounter.values())
                .filter(discounter -> discounter.condition.test(age))
                .map(discounter -> discounter.discount.apply(fare))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이 정보가 올바르지 않습니다."));
    }
}
