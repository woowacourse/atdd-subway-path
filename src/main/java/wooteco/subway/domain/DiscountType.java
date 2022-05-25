package wooteco.subway.domain;

import static wooteco.subway.domain.DiscountType.Constant.CHILDREN_MAXIMUM_AGE;
import static wooteco.subway.domain.DiscountType.Constant.CHILDREN_MINIMUM_AGE;
import static wooteco.subway.domain.DiscountType.Constant.SENILE_MINIMUM_AGE;
import static wooteco.subway.domain.DiscountType.Constant.TEENAGER_MAXIMUM_AGE;
import static wooteco.subway.domain.DiscountType.Constant.TEENAGER_MINIMUM_AGE;

import java.util.Arrays;
import java.util.function.Function;
import wooteco.subway.domain.farediscount.ChildrenDiscountPolicy;
import wooteco.subway.domain.farediscount.DiscountPolicy;
import wooteco.subway.domain.farediscount.NormalDiscountPolicy;
import wooteco.subway.domain.farediscount.SpecialDiscountPolicy;
import wooteco.subway.domain.farediscount.TeenagerDiscountPolicy;

public enum DiscountType {

    CHILD_DISCOUNT(new ChildrenDiscountPolicy(), (age) -> age >= CHILDREN_MINIMUM_AGE && age < CHILDREN_MAXIMUM_AGE),
    TEENAGER_DISCOUNT(new TeenagerDiscountPolicy(), (age) -> age >= TEENAGER_MINIMUM_AGE && age < TEENAGER_MAXIMUM_AGE),
    SPECIAL_DISCOUNT(new SpecialDiscountPolicy(), (age) -> age >= SENILE_MINIMUM_AGE || age < CHILDREN_MINIMUM_AGE);

    private final DiscountPolicy discountPolicy;
    private final Function<Integer, Boolean> expression;

    DiscountType(DiscountPolicy discountPolicy,
        Function<Integer, Boolean> expression) {
        this.discountPolicy = discountPolicy;
        this.expression = expression;
    }

    public static DiscountPolicy findDiscountPolicy(int age) {
        return Arrays.stream(values())
            .filter(i -> i.getExpression().apply(age))
            .findAny()
            .map(DiscountType::getDiscountPolicy)
            .orElse(new NormalDiscountPolicy());
    }

    public Function<Integer, Boolean> getExpression() {
        return expression;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    static class Constant {
        static final int CHILDREN_MINIMUM_AGE = 6;
        static final int CHILDREN_MAXIMUM_AGE = 13;
        static final int TEENAGER_MINIMUM_AGE = 6;
        static final int TEENAGER_MAXIMUM_AGE = 13;
        static final int SENILE_MINIMUM_AGE = 65;
    }
}
