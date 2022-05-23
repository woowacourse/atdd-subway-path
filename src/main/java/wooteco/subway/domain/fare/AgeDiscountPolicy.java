package wooteco.subway.domain.fare;

public class AgeDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCT = 350;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    @Override
    public Fare discountFare(Fare fare, int age) {
        if(isChild(age)){
            return fare.discount(DEDUCT, CHILD_DISCOUNT_RATE);
        }
        if (isTeenager(age)){
            return fare.discount(DEDUCT, TEENAGER_DISCOUNT_RATE);
        }
        return fare;
    }

    private boolean isChild(int age) {
        return 6 <= age && age < 13;
    }

    private boolean isTeenager(int age) {
        return 13 <= age && age < 19;
    }
}
