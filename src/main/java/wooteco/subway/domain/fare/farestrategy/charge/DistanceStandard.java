package wooteco.subway.domain.fare.farestrategy.charge;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.farestrategy.ChargeStrategy;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.NotFoundException;

public enum DistanceStandard {
    DEFAULT(x -> 0 < x && x <= 10, DefaultDistanceStrategy::new),
    ADDITIONAL(x -> 10 < x && x <= 50, AdditionalDistanceStrategy::new),
    MAXIMUM(x -> 50 < x, MaximumDistanceStrategy::new);

    private final Predicate<Integer> distancePredicate;
    private final Function<Integer, ChargeStrategy> chargeStrategyFunction;

    DistanceStandard(Predicate<Integer> distancePredicate,
                     Function<Integer, ChargeStrategy> chargeStrategyFunction) {
        this.distancePredicate = distancePredicate;
        this.chargeStrategyFunction = chargeStrategyFunction;
    }

    public static ChargeStrategy findStrategy(int distance) {
        return Arrays.stream(values())
                .filter(it -> it.distancePredicate.test(distance))
                .findFirst()
                .orElseThrow(() -> new DomainException(ExceptionMessage.INVALID_DISTANCE.getContent()))
                .chargeStrategyFunction.apply(distance);
    }
}
