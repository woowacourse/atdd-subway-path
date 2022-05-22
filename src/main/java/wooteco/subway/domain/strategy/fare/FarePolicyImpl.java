package wooteco.subway.domain.strategy.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.strategy.fare.basic.DistanceFareStrategy;
import wooteco.subway.domain.strategy.fare.basic.DistanceFareStrategyFactory;
import wooteco.subway.domain.strategy.fare.discount.DiscountStrategy;
import wooteco.subway.domain.strategy.fare.discount.DiscountStrategyFactory;

@Component
public class FarePolicyImpl extends FarePolicy{

    @Override
    protected int calculateBasicFare(int distance) {
        DistanceFareStrategy distanceFareStrategy = DistanceFareStrategyFactory.getDistanceFareStrategy(distance);
        return distanceFareStrategy.calculateFare(distance);
    }

    @Override
    protected int calculateExtraFare(List<Integer> extraPrices) {
        return extraPrices.stream()
                .mapToInt(extraPrice -> extraPrice)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("가장 큰 값이 존재하지 않습니다"));
    }

    @Override
    protected int calculateDiscountFare(int age) {
        DiscountStrategy discountStrategy = DiscountStrategyFactory.getDiscountStrategy(age);
        return discountStrategy.calculateDiscount(age);
    }
}
