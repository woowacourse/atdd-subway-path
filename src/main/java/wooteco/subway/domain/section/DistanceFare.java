package wooteco.subway.domain.section;

import java.util.function.Function;
import wooteco.subway.domain.path.Fare;

public enum DistanceFare {
    OVER_TEN_KM {
        @Override
        protected int calculate(double distance) {
            if (distance > 50) {
                return calculate(50);
            }
            double extraDistance = distance - 10;
            return calculate(extraDistance, 5, 100);
        }
    },
    OVER_FIFTY_KM {
        @Override
        protected int calculate(double distance) {
            double extraDistance = distance - 50;
            return calculate(extraDistance, 8, 100);
        }
    };

    private static final int BASIC_FARE = 1250;

    static Function<Double, Fare> fareCalculator() {
        return (distance) -> {
            int totalAmount = BASIC_FARE + OVER_TEN_KM.calculate(distance) + OVER_FIFTY_KM.calculate(distance);
            return new Fare(totalAmount);
        };
    }

    protected abstract int calculate(double distance);

    int calculate(double distance, int unit, int amount) {
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil(distance / unit)) * amount);
    }
}
