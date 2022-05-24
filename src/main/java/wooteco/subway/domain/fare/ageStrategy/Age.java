package wooteco.subway.domain.fare.ageStrategy;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Age {
    PREFERENTIAL (age -> age < 6 || age >= 65, new PreferentialDiscountPolicy()),
    CHILDREN (age -> age >= 6 && age < 13, new ChildrenDiscountPolicy()),
    TEENAGER (age -> age >= 13 && age <= 18, new TeenagerDiscountPolicy()),
    NORMAL (age -> age > 18, new NormalAgeDiscountPolicy())
    ;

    private final Predicate<Integer> ageCondition;
    private final AgeDiscountPolicy ageDiscountPolicy;

    Age(Predicate<Integer> ageCondition, AgeDiscountPolicy ageDiscountPolicy) {
        this.ageCondition = ageCondition;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public static AgeDiscountPolicy of(int rawAge) {
        return Arrays.stream(Age.values())
            .filter(age -> age.ageCondition.test(rawAge))
            .findFirst()
            .map(age -> age.ageDiscountPolicy)
            .orElseThrow(() -> new IllegalArgumentException("나이가 잘못 입력되었습니다."));
    }
}
