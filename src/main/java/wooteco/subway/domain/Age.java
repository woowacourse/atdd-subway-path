package wooteco.subway.domain;

public enum Age {

    INFANT(0L, 5L, 0),
    CHILDREN(6L, 12L, 0.5),
    TEENAGER(13L, 18L, 0.8);

    private final Long minAge;
    private final Long maxAge;
    private final double discountRate;

    Age(Long minAge, Long maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public Boolean isInclude(Long age) {
        return age >= minAge && age <= maxAge;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
