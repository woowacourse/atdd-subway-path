package wooteco.subway.domain;

import java.util.function.Function;

public enum DistanceFare {
    OVER_TEN_KM {
        @Override
        Fare calculate(double distance) {
            if (distance > 50) {
                return calculate(50);
            }
            double extraDistance = distance - 10;
            return calculate(extraDistance, 5, 100);
        }
    },
    OVER_FIFTY_KM {
        @Override
        Fare calculate(double distance) {
            double extraDistance = distance - 50;
            return calculate(extraDistance, 8, 100);
        }
    }
    ;

    private static final Fare BASIC_FARE = new Fare(1250);

    public static Function<Double, Fare> fareCalculator() {
        return (distance) -> Fare.sum(BASIC_FARE, OVER_TEN_KM.calculate(distance), OVER_FIFTY_KM.calculate(distance));
    }

    abstract Fare calculate(double distance);

    Fare calculate(double distance, int unit, int amount) {
        if (distance <= 0) {
            return new Fare(0);
        }
        return new Fare((int) ((Math.ceil(distance / unit)) * amount));
    }
}
