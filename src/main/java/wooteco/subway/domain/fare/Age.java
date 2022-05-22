package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.strategy.*;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Age {
    BABY(age -> 0 <= age && age <= 5, new BabyFare()),
    CHILD(age -> 6 <= age && age <= 12, new ChildFare()),
    TEEN(age -> 13 <= age && age <= 18, new TeenFare()),
    ADULT(age -> 19 <= age, new AdultFare()),
    ;

    private final Predicate<Integer> judgeAge;
    private final AgeStrategy ageStrategy;

    Age(Predicate<Integer> judgeAge, AgeStrategy ageStrategy) {
        this.judgeAge = judgeAge;
        this.ageStrategy = ageStrategy;
    }

    public static AgeStrategy from(int age) {
        return Arrays.stream(Age.values())
                .filter(agejudge -> agejudge.judgeAge.test(age))
                .findFirst()
                .map(findAgeRange -> findAgeRange.ageStrategy)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 나이입니다."));
    }
}
