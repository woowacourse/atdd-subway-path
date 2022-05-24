package wooteco.subway.domain.fare;

import wooteco.subway.domain.Lines;
import wooteco.subway.domain.fare.ageStrategy.AgeDiscountFactory;
import wooteco.subway.domain.fare.ageStrategy.AgeDiscountPolicy;
import wooteco.subway.domain.fare.distanceStrategy.DistanceDiscountFactory;
import wooteco.subway.domain.fare.distanceStrategy.DistanceDiscountPolicy;

public class Fare {
    private final int amount;

    private Fare(int amount) {
        this.amount = amount;
    }

    public static Fare from(int distance, Lines lines, int age) {
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountFactory.from(age);
        DistanceDiscountPolicy distanceDiscountPolicy = DistanceDiscountFactory.from(distance);
        FareCalculator fareCalculator = new FareCalculator(ageDiscountPolicy, distanceDiscountPolicy);
        return new Fare((int) fareCalculator.calculate(distance, lines));
    }

    public int getAmount() {
        return amount;
    }
}
