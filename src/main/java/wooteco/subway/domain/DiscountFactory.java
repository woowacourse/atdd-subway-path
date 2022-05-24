package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import wooteco.subway.domain.farediscount.ChildrenDiscountPolicy;
import wooteco.subway.domain.farediscount.DiscountPolicy;
import wooteco.subway.domain.farediscount.NormalDiscountPolicy;
import wooteco.subway.domain.farediscount.SpecialDiscountPolicy;
import wooteco.subway.domain.farediscount.TeenagerDiscountPolicy;

public enum DiscountFactory {

    CHILD(new ChildrenDiscountPolicy(), (age) -> age >= 6 && age < 13),
    TEENAGER(new TeenagerDiscountPolicy(), (age) -> age >= 13 && age < 19),
    SPECIAL(new SpecialDiscountPolicy(), (age) -> age >= 65 || age < 6);

    private final DiscountPolicy discountPolicy;
    private final Function<Integer, Boolean> expression;


    DiscountFactory(DiscountPolicy discountPolicy,
        Function<Integer, Boolean> expression) {
        this.discountPolicy = discountPolicy;
        this.expression = expression;
    }

    public static DiscountPolicy findDiscountPolicy(int age) {
        return Arrays.stream(values())
            .filter(i -> i.getExpression().apply(age))
            .findAny()
            .map(DiscountFactory::getDiscountPolicy)
            .orElse(new NormalDiscountPolicy());
    }

    public Function<Integer, Boolean> getExpression() {
        return expression;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}
