package wooteco.subway.domain;

import java.util.Arrays;

public enum Age {

    INFANT(5, 0, 0),
    CHILDREN(12, 350, 0.5),
    TEENAGER(18, 350, 0.8),
    ADULT(79, 0, 1),
    ELDER(80, 0, 0);

    private final int boundary;
    private final int deduction;
    private final double discountRate;

    Age(int boundary, int deduction, double discountRate) {
        this.boundary = boundary;
        this.deduction = deduction;
        this.discountRate = discountRate;
    }

    public static Age findByAge(Long number) {
        return Arrays.stream(Age.values())
                .filter(age -> number <= age.boundary)
                .findFirst()
                .orElse(ELDER);
    }

    public double calc(int money) {
        return (money - deduction) * discountRate;
    }

    public int getBoundary() {
        return boundary;
    }
}
