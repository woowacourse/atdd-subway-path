package wooteco.subway.domain.fare.policy.age;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.policy.FarePolicy;

public enum AgeDiscountPolicyGenerator {
    BABY((age) -> 0 <= age && age <= 5,
            new BabyDiscountPolicy()),
    CHILD((age) -> 5 < age && age <= 12,
            new ChildDiscountPolicy()),
    TEENAGER((age) -> 12 < age && age <= 18,
            new TeenagerDiscountPolicy()),
    ADULT((age) -> age > 18,
            new AdultDiscountPolicy());

    private static final String NO_AGE_DISCOUNT_POLICY_ERROR_MESSAGE = "해당되는 나이 정책이 없습니다.";

    private final Predicate<Integer> predicate;
    private final FarePolicy ageDiscountPolicy;

    AgeDiscountPolicyGenerator(Predicate<Integer> predicate, FarePolicy ageDiscountPolicy) {
        this.predicate = predicate;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public static FarePolicy of(int age) {
        return Arrays.stream(AgeDiscountPolicyGenerator.values())
                .filter(generator -> generator.predicate.test(age))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_AGE_DISCOUNT_POLICY_ERROR_MESSAGE))
                .ageDiscountPolicy;
    }
}
