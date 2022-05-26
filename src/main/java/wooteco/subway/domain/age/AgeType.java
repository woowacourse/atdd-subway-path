package wooteco.subway.domain.age;

import java.util.Arrays;

public enum AgeType {

    BABY(1, 5),
    KIDS(6, 12),
    TEENAGER(13, 18),
    ADULT(19, Integer.MAX_VALUE);

    private final int minValue;
    private final int maxValue;

    AgeType(final int minValue, final int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static AgeType from(final int age) {
        return Arrays.stream(values())
                .filter(ageType -> ageType.isType(age))
                .findFirst()
                .orElseThrow();
    }

    private boolean isType(final int age) {
        return age >= minValue && age <= maxValue;
    }
}
