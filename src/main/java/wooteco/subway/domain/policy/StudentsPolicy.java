package wooteco.subway.domain.policy;

import wooteco.subway.domain.fare.Fare;

public class StudentsPolicy extends NeedCalculateDiscountPolicy {


    public static final double STUDENT_DISCOUNT_RATE = 0.8;

    @Override
    public int calculateDiscountFare(Fare fare) {
        return (int) ((fare.calculateFare() - BASE_FARE) * STUDENT_DISCOUNT_RATE);
    }
}
