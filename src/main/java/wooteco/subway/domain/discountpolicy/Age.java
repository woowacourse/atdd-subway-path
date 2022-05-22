package wooteco.subway.domain.discountpolicy;

import java.util.Arrays;

public enum Age {

    PRESCHOOLER(1, 5),
    CHILD(6, 12),
    TEEN(13, 18),
    ADULT(19, Integer.MAX_VALUE)
    ;

    private final int minRange;
    private final int maxRage;

    Age(final int minRange, final int maxRage) {
        this.minRange = minRange;
        this.maxRage = maxRage;
    }

    public static Age from(final int targetAge) {
        return Arrays.stream(values())
                .filter(age -> age.minRange <= targetAge && targetAge <= age.maxRage)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
