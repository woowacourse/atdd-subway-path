package wooteco.subway.domain;

import java.math.BigDecimal;

public enum Passenger {
    CHILD(BigDecimal.valueOf(350), BigDecimal.valueOf(0.5)),
    YOUTH(BigDecimal.valueOf(350), BigDecimal.valueOf(0.8)),
    ORDINAL(BigDecimal.valueOf(0), BigDecimal.valueOf(1.0));

    private final BigDecimal discountAmount;
    private final BigDecimal inverseOfDiscountRate;

    Passenger(BigDecimal discountAmount, BigDecimal inverseOfDiscountRate) {
        this.discountAmount = discountAmount;
        this.inverseOfDiscountRate = inverseOfDiscountRate;
    }

    public int calculateFare(int totalFare) {
        return BigDecimal.valueOf(totalFare)
            .subtract(discountAmount)
            .multiply(inverseOfDiscountRate)
            .intValue();
    }

    public static Passenger valueOf(int age) {
        if (13 <= age && age < 19) {
            return YOUTH;
        }
        if (6 <= age && age < 13) {
            return CHILD;
        }
        return ORDINAL;
    }
}
