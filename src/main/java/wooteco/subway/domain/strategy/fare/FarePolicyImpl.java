package wooteco.subway.domain.strategy.fare;

import java.util.List;
import wooteco.subway.domain.strategy.fare.discount.DiscountManager;
import wooteco.subway.domain.strategy.fare.discount.DiscountManagerFactory;
import wooteco.subway.domain.strategy.fare.distance.DistanceFareManager;
import wooteco.subway.domain.strategy.fare.distance.DistanceFareManagerFactory;

public class FarePolicyImpl extends FarePolicy {

    @Override
    protected int calculateBasicFare(int distance) {
        DistanceFareManager distanceFareManager = DistanceFareManagerFactory.createDistanceFareManager();
        return distanceFareManager.calculateFare(distance);
    }

    @Override
    protected int calculateExtraFare(List<Integer> extraPrices) {
        return extraPrices.stream()
                .mapToInt(extraPrice -> extraPrice)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("가장 큰 값이 존재하지 않습니다"));
    }

    @Override
    protected int calculateDiscountFare(int price, int age) {
        DiscountManager ageDiscountManager = DiscountManagerFactory.createDiscountManager();
        return ageDiscountManager.calculateDiscount(age, price);
    }
}
