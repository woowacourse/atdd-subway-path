package wooteco.subway.domain.property.fare;

import wooteco.subway.domain.property.Age;

public class AgeFarePolicy implements FarePolicy {

    private static final int DEFAULT_DISCOUNT = 350;

    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    private final Age age;

    public AgeFarePolicy(Age age) {
        this.age = age;
    }

    @Override
    public int apply(int fare) {
        if (age.isTeenager()) {
            return fare - ((int)Math.round((fare - DEFAULT_DISCOUNT) * TEENAGER_DISCOUNT_RATE) + DEFAULT_DISCOUNT);
        }
        if (age.isChild()) {
            return fare - ((int)Math.round((fare - DEFAULT_DISCOUNT) * CHILD_DISCOUNT_RATE) + DEFAULT_DISCOUNT);
        }
        return fare;
    }
}
