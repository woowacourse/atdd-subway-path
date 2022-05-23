package wooteco.subway.domain.fare;

public class FirstOverFareStrategy extends OverFareStrategy {

    @Override
    public int calculateFare(final FareCondition fareCondition) {
        int fare = DEFAULT_FARE
                + calculateOverFare(fareCondition.getDistance() - DEFAULT_DISTANCE, STANDARD_UNIT)
                + fareCondition.getExtraFare();
        return getAgeDiscountPolicy(fareCondition.getAge()).discount(fare);
    }
}
