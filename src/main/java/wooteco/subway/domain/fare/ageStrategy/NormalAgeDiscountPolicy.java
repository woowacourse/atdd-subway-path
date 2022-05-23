package wooteco.subway.domain.fare.ageStrategy;

public class NormalAgeDiscountPolicy implements AgeDiscountPolicy {
    @Override
    public int calculateFare(int fare) {
        return fare;
    }
}
