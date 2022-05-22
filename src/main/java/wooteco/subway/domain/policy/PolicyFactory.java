package wooteco.subway.domain.policy;

import java.util.List;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.fare.DistancePolicy;
import wooteco.subway.domain.policy.age.BabyDiscountPolicy;
import wooteco.subway.domain.policy.age.ChildDiscountPolicy;
import wooteco.subway.domain.policy.age.DefaultDiscountPolicy;
import wooteco.subway.domain.policy.age.TeenagerDiscountPolicy;
import wooteco.subway.domain.policy.distance.OverFiftyKMPolicy;
import wooteco.subway.domain.policy.distance.TenToFiftyKMPolicy;
import wooteco.subway.domain.policy.distance.UnderTenKMPolicy;

public class PolicyFactory {
    private static final int BABY_MAX_AGE = 6;
    private static final int CHILD_MAX_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 19;

    private static final int OVER_TEN_DISTANCE = 10;
    private static final int OVER_FIFTY_DISTANCE = 50;

    public static DistancePolicy createDistance(int distance) {
        if (distance <= OVER_TEN_DISTANCE) {
            return new UnderTenKMPolicy();
        }
        if (distance <= OVER_FIFTY_DISTANCE) {
            return new TenToFiftyKMPolicy(distance);
        }
        return new OverFiftyKMPolicy(distance);
    }

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
