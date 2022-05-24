package wooteco.subway.domain.fare.ageStrategy;

public class PreferentialDiscountPolicy implements AgeDiscountPolicy {
    @Override
    public int calculateFare(int fare) {
        return 0;
    }
}
