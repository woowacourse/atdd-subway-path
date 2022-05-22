package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeType {
    INFANT(age -> age > 0 && age < 6, originalFare -> 0),
    CHILD(age -> age >= 6 && age < 13, originalFare -> (int) ((originalFare - 350) * 0.5)),
    TEENAGER(age -> age >= 13 && age < 19, originalFare -> (int) ((originalFare - 350) * 0.8)),
    ADULT(age -> age >= 19, originalFare -> originalFare);

    private final Predicate<Integer> agePredicate;
    private final Function<Integer, Integer> originalFare;

    AgeType(Predicate<Integer> agePredicate, Function<Integer, Integer> originalFare) {
        this.agePredicate = agePredicate;
        this.originalFare = originalFare;
    }

    public static AgeType from(int age) {
        return Arrays.stream(values())
                .filter(it -> it.agePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 나이는 존재하지 않습니다."));
    }

    public int calculateFare(int originFare) {
        return originalFare.apply(originFare);
    }
}
