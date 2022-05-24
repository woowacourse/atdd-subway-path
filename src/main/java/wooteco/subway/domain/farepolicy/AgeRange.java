package wooteco.subway.domain.farepolicy;

import java.util.Arrays;

public enum AgeRange {

    EARLY_CHILDHOOD(1, 5),
    CHILDREN(6, 12),
    TEENAGER(13, 18),
    ADULT(19, 64),
    ELDER(65, Integer.MAX_VALUE);

    private final int minAge;
    private final int maxAge;

    AgeRange(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static AgeRange from(final int age) {
        return Arrays.stream(values())
                .filter(ageRange -> ageRange.minAge <= age && age <= ageRange.maxAge)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이는 1 이상의 정수여야 합니다."));
    }
}
