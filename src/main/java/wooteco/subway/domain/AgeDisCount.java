package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeDisCount {

    BABY(age -> 0 <= age && age <= 5),
    CHILDREN(age -> 6 <= age && age <= 12),
    TEENAGER(age -> 13 <= age && age <= 18),
    ADULT(age -> 19 <= age),
    ;

    private final Predicate<Integer> containsAgePredicate;

    AgeDisCount(final Predicate<Integer> containsAgePredicate) {
        this.containsAgePredicate = containsAgePredicate;
    }

    public static AgeDisCount from(final int age) {
        return Arrays.stream(values())
                .filter(ageDisCount -> ageDisCount.containsAgePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("정상적인 사람 나이가 아닙니다."));
    }
}
