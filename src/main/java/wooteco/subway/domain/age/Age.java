package wooteco.subway.domain.age;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Age {

    BABY((age) -> age >= Constants.BABY_MIN_AGE && age <= Constants.BABY_MAX_AGE),
    KIDS((age) -> age >= Constants.KIDS_MIN_AGE && age <= Constants.KIDS_MAX_AGE),
    TEENAGER((age) -> age >= Constants.TEENAGER_MIN_AGE && age < Constants.TEENAGER_MAX_AGE),
    ADULT((age) -> Constants.ADULT_MIN_AGE <= age);

    private final Predicate<Integer> rangePredicate;

    Age(Predicate<Integer> rangePredicate) {
        this.rangePredicate = rangePredicate;
    }

    public static Age from(int age) {
        return Arrays.stream(values())
                .filter(it -> it.rangePredicate.test(age))
                .findFirst()
                .orElseThrow();
    }

    private static class Constants {
        private static final int BABY_MIN_AGE = 1;
        private static final int BABY_MAX_AGE = 5;
        private static final int KIDS_MIN_AGE = 6;
        private static final int KIDS_MAX_AGE = 12;
        private static final int TEENAGER_MIN_AGE = 13;
        private static final int TEENAGER_MAX_AGE = 19;
        private static final int ADULT_MIN_AGE = 19;
    }
}
