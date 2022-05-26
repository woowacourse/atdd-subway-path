package wooteco.subway.domain;

import java.util.Arrays;
import wooteco.subway.exception.ClientException;

public enum AgeRange {

    INFANT(0L, 5L, 0),
    CHILDREN(6L, 12L, 0.5),
    TEENAGER(13L, 18L, 0.8),
    ADULT(19L, Long.MAX_VALUE, 1);

    private final Long minAge;
    private final Long maxAge;
    private final double discountRate;

    AgeRange(Long minAge, Long maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static AgeRange findRange(Long age) {
        return Arrays.stream(AgeRange.values())
                .filter(it -> it.isInclude(age))
                .findFirst()
                .orElseThrow(() -> new ClientException("[ERROR] 존재하지 않는 범위입니다."));
    }

    public Boolean isInclude(Long age) {
        return age >= minAge && age <= maxAge;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
