package wooteco.subway.domain.policy;

import java.util.List;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.policy.age.BabyDiscountPolicy;
import wooteco.subway.domain.policy.age.ChildDiscountPolicy;
import wooteco.subway.domain.policy.age.DefaultDiscountPolicy;
import wooteco.subway.domain.policy.age.TeenagerDiscountPolicy;

public class PolicyFactory {
    private static final int BABY_MAX_AGE = 6;
    private static final int CHILD_MAX_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 19;

    public static AgeDiscountPolicy createAgeDiscount(int age) {
        if (age < BABY_MAX_AGE) {
            return new BabyDiscountPolicy();
        }
        if (age < CHILD_MAX_AGE) {
            return new ChildDiscountPolicy();
        }
        if (age < TEENAGER_MAX_AGE) {
            return new TeenagerDiscountPolicy();
        }
        return new DefaultDiscountPolicy();
    }

    public static LinePolicy createLineFee(List<Line> lines) {
        return new LinePolicy(lines);
    }
}
