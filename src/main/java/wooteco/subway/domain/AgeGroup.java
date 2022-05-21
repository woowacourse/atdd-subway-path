package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeGroup {
    BABY(age -> 0 <= age && age < 6),
    CHILDREN(age -> 6 <= age && age < 13),
    TEENAGER(age -> 13 <= age && age < 19),
    ADULT(age -> 19 <= age);

    private final Predicate<Integer> grouping;

    AgeGroup(Predicate<Integer> grouping) {
        this.grouping = grouping;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(AgeGroup.values())
                .filter(ageGroup -> ageGroup.grouping.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 연령대가 존재하지 않습니다."));
    }
}
