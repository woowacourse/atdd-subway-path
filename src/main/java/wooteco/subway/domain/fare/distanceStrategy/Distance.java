package wooteco.subway.domain.fare.distanceStrategy;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Distance {
    BELOW_MINIMUM (discount -> discount < 10, new FreeDiscountPolicy()),
    BELOW_MAXIMUM (discount -> 10 <= discount && discount < 50, new NormalDistanceDiscountPolicy()),
    OVER_MAXIMUM (discount -> 50 <= discount, new ExtraDiscountPolicy())
    ;

    private final Predicate<Integer> discountCondition;
    private final DistanceDiscountPolicy distanceDiscountPolicy;

    Distance(Predicate<Integer> discountCondition,
        DistanceDiscountPolicy distanceDiscountPolicy) {
        this.discountCondition = discountCondition;
        this.distanceDiscountPolicy = distanceDiscountPolicy;
    }

    public static DistanceDiscountPolicy of(int rawDistance) {
        return Arrays.stream(Distance.values())
            .filter(distance -> distance.discountCondition.test(rawDistance))
            .map(distance -> distance.distanceDiscountPolicy)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("거리가 잘못 입력되었습니다."));
    }
}
