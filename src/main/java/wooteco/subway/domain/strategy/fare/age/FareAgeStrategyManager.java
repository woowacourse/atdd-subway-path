package wooteco.subway.domain.strategy.fare.age;

import java.util.List;
import wooteco.subway.exception.FareAgeStrategyNotFoundException;

public class FareAgeStrategyManager {

    private final List<FareDiscountAgeStrategy> fareDiscountAgeStrategies;

    public FareAgeStrategyManager(List<FareDiscountAgeStrategy> fareDiscountAgeStrategies) {
        this.fareDiscountAgeStrategies = fareDiscountAgeStrategies;
    }

    public int calculateDiscountAgeFare(int age, int totalPrice) {
        return fareDiscountAgeStrategies.stream()
                .filter(it -> it.isApplied(age))
                .map(it -> it.calculateDiscount(totalPrice))
                .findFirst()
                .orElseThrow(() -> new FareAgeStrategyNotFoundException());
    }
}
