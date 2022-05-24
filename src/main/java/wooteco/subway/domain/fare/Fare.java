package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.discountstrategy.DiscountStrategy;
import wooteco.subway.domain.fare.distancestrategy.BasicDistanceStrategy;
import wooteco.subway.domain.fare.distancestrategy.DistanceStrategy;
import wooteco.subway.domain.fare.distancestrategy.FirstDistanceStrategy;
import wooteco.subway.domain.fare.distancestrategy.SecondDistanceFareStrategy;

public class Fare {

    private static final String DISTANCE_NEGATIVE_ERROR = "거리는 음수가 될 수 없습니다.";

    private final int distance;
    private final DiscountStrategy discountStrategy;

    public Fare(int distance, DiscountStrategy discountStrategy) {
        validateDistance(distance);
        this.distance = distance;
        this.discountStrategy = discountStrategy;
    }

    private void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(DISTANCE_NEGATIVE_ERROR);
        }
    }

    public int calculate(int extraFare) {
        int fare = getDistanceStrategy().getPrice(distance) + extraFare;
        return discountStrategy.getDiscountedFare(fare);
    }

    private DistanceStrategy getDistanceStrategy() {
        if (distance > 10 && distance <= 50) {
            return new FirstDistanceStrategy();
        }
        if (distance > 50) {
            return new SecondDistanceFareStrategy();
        }
        return new BasicDistanceStrategy();
    }
}
