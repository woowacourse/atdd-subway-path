package wooteco.subway.domain.fare.policy.age;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.policy.FarePolicy;

public enum AgeDiscountPolicyGenerator {
    BABY((age) ->
            Constants.MIN_AGE <= age && age <= Constants.BABY_MAX_AGE,
            new BabyDiscountPolicy()),
    CHILD((age) ->
            Constants.BABY_MAX_AGE < age && age <= Constants.CHILD_MAX_AGE,
            new ChildDiscountPolicy()),
    TEENAGER((age) ->
            Constants.CHILD_MAX_AGE < age && age <= Constants.TEENAGER_MAX_AGE,
            new TeenagerDiscountPolicy()),
    ADULT((age) ->
            age > Constants.TEENAGER_MAX_AGE,
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

    private static class Constants {
        private static final int MIN_AGE = 0;
        private static final int BABY_MAX_AGE = 5;
        private static final int CHILD_MAX_AGE = 12;
        private static final int TEENAGER_MAX_AGE = 18;
    }
}
