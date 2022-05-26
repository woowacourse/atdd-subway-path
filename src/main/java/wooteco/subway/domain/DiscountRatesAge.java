package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountRatesAge {
    ADULT(age -> 19 <= age, 0),
    TEENAGER(age -> 13 <= age, 0.2),
    CHILD(age -> 0 < age && age < 13, 0.5),
    ;

    private final Predicate<Integer> predicate;
    private final double discountRate;

    DiscountRatesAge(Predicate<Integer> predicate, double discountRate) {
        this.predicate = predicate;
        this.discountRate = discountRate;
    }

    public static DiscountRatesAge valueOf(int age) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("적절한 연령이 입력되지 않았습니다."));
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
