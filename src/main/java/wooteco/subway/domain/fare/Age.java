package wooteco.subway.domain.fare;

import java.util.Arrays;

public enum Age {
    CHILD(6, 12),
    ADOLESCENT(13, 18),
    OTHERS(0, 0);

    private final int minAge;
    private final int maxAge;

    Age(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static Age from(int age) {
        return Arrays.stream(values())
                .filter(it -> age>= it.minAge && age <= it.maxAge)
                .findFirst()
                .orElse(OTHERS);
    }
}
