package wooteco.subway.domain.policy;

public class AdultPolicy implements DiscountPolicy {

    private static final int ADULT_START_AGE = 19;

    @Override
    public int calculateDiscountFare(int fare) {
        return fare;
    }

    @Override
    public boolean checkAgeRange(int age) {
        return ADULT_START_AGE <= age;
    }
}
