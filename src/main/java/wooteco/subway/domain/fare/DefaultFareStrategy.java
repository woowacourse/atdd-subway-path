package wooteco.subway.domain.fare;

public class DefaultFareStrategy extends FareStrategy {

    @Override
    public int calculateFare(final FareCondition fareCondition) {
        return getAgeDiscountPolicy(fareCondition.getAge())
                .discount(DEFAULT_FARE + fareCondition.getExtraFare());
    }
}
