package wooteco.subway.domain.policy;

public class StudentsPolicy extends NeedCalculateDiscountPolicy {

    private static final double STUDENT_DISCOUNT_RATE = 0.8;
    private static final double STUDENT_AGE_START = 13;
    private static final double STUDENT_AGE_END = 19;

    @Override
    public int calculateDiscountFare(int fare) {
        return (int) ((fare - BASE_FARE) * STUDENT_DISCOUNT_RATE);
    }

    @Override
    public boolean checkAgeRange(int age) {
        return age >= STUDENT_AGE_START && age < STUDENT_AGE_END;
    }
}
