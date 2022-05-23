package wooteco.subway.domain.fare;

import wooteco.subway.domain.Lines;
import wooteco.subway.domain.fare.ageStrategy.AgeDiscountPolicy;
import wooteco.subway.domain.fare.distanceStrategy.DistanceDiscountPolicy;

public class FareCalculator {
    private static final int BASE_FARE = 1250;

    private final AgeDiscountPolicy ageDiscountPolicy;
    private final DistanceDiscountPolicy distanceDiscountPolicy;

    public FareCalculator(AgeDiscountPolicy ageDiscountPolicy,
        DistanceDiscountPolicy distanceDiscountPolicy) {
        this.ageDiscountPolicy = ageDiscountPolicy;
        this.distanceDiscountPolicy = distanceDiscountPolicy;
    }

    public double calculate(int distance, Lines lines) {
        return ageDiscountPolicy.calculateFare(
            distanceDiscountPolicy.calculateFare(distance, BASE_FARE) + lines.findMaxExtraFare()
        );
    }
}
