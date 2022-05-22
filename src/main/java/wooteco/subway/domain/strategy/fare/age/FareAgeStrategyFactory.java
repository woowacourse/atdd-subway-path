package wooteco.subway.domain.strategy.fare.age;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.exception.FareAgeStrategyNotFoundException;

public enum FareAgeStrategyFactory {

    INFANT(FareAgeStrategyFactory::isInfant, new InfantStrategy()),
    CHILD(FareAgeStrategyFactory::isChild, new ChildStrategy()),
    TEENAGE(FareAgeStrategyFactory::isTeenage, new TeenAgeStrategy()),
    ADULT(FareAgeStrategyFactory::isAdult, new AdultStrategy());

    private final Predicate<Integer> predicate;
    private final FareDiscountAgeStrategy fareAgeStrategy;

    FareAgeStrategyFactory(Predicate<Integer> predicate, FareDiscountAgeStrategy fareAgeStrategy) {
        this.predicate = predicate;
        this.fareAgeStrategy = fareAgeStrategy;
    }

    private static boolean isInfant(Integer age) {
        return age < 6;
    }

    private static boolean isChild(Integer age) {
        return age >= 6 && age < 13;
    }

    private static boolean isTeenage(Integer age) {
        return age >= 13 && age <= 19;
    }

    private static boolean isAdult(Integer age) {
        return age > 19;
    }

    public static FareDiscountAgeStrategy getStrategy(int age) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(age))
                .findFirst()
                .orElseThrow(FareAgeStrategyNotFoundException::new)
                .fareAgeStrategy;
    }
}
