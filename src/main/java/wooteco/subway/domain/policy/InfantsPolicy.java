package wooteco.subway.domain.policy;

public class InfantsPolicy implements DiscountPolicy {

    private static final int FREE_FARE = 0;
    private static final int INFANT_START_AGE = 0;
    private static final int INFANT_END_AGE = 6;

    @Override
    public int calculateDiscountFare(int fare) {
        return FREE_FARE;
    }

    @Override
    public boolean checkAgeRange(int age) {
        return INFANT_START_AGE <= age && age < INFANT_END_AGE;
    }
}
