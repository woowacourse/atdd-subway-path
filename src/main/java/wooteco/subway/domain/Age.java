package wooteco.subway.domain;

import wooteco.subway.exception.NotFoundException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Age {

    PRESCHOOLER(Age::isPreschooler),
    CHILD(Age::isChild),
    TEENAGER(Age::isTeenager),
    ADULT(Age::isAdult);

    private final Predicate<Integer> isMatchAge;

    Age(Predicate<Integer> isMatchAge) {
        this.isMatchAge = isMatchAge;
    }

    private static boolean isPreschooler(int age) {
        return age < 6;
    }

    private static boolean isChild(int age) {
        return 6 <= age && age < 13;
    }

    private static boolean isTeenager(int age) {
        return 13 <= age && age < 19;
    }

    private static boolean isAdult(int age) {
        return 19 <= age;
    }

    public static Age findAgePolicy(int age) {
        return Arrays.stream(values())
                .filter(v -> v.isMatchAge.test(age))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("해당하는 연령대를 찾지 못하였습니다."));
    }
}
