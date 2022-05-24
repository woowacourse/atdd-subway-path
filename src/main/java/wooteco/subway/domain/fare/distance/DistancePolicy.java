package wooteco.subway.domain.fare.distance;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public enum DistancePolicy {

    DEFAULT(DistancePolicy::isDefault, new DefaultFareStrategy()),
    MEDIUM(DistancePolicy::isMedium, new MediumFareStrategy()),
    MAXIMUM(DistancePolicy::isMaximum, new MaximumFareStrategy()),
    ;

    private final Predicate<Double> predicate;
    private final DistanceFareStrategy distanceFareStrategy;

    DistancePolicy(
        final Predicate<Double> predicate,
        final DistanceFareStrategy distanceFareStrategy
    ) {
        this.predicate = predicate;
        this.distanceFareStrategy = distanceFareStrategy;
    }

    public static int calculate(final double distance) {
        return Arrays.stream(DistancePolicy.values())
            .filter(distancePolicy -> distancePolicy.predicate.test(distance))
            .map(distancePolicy -> distancePolicy.distanceFareStrategy.calculate(distance))
            .findFirst()
            .orElseThrow(NoSuchElementException::new);
    }

    private static boolean isDefault(final double distance) {
        return distance <= 10;
    }

    private static boolean isMedium(final double distance) {
        return distance > 10 && distance <= 50;
    }

    private static boolean isMaximum(final double distance) {
        return distance > 50;
    }
}
