package wooteco.subway.domain;

import static java.math.BigDecimal.ONE;

import java.math.BigDecimal;

public enum Passenger {
    CHILD(BigDecimal.valueOf(350), BigDecimal.valueOf(0.5)),
    YOUTH(BigDecimal.valueOf(350), BigDecimal.valueOf(0.2)),
    ORDINAL(BigDecimal.valueOf(0), BigDecimal.valueOf(0.0));

    private final BigDecimal deductionAmount;
    private final BigDecimal discountRate;

    Passenger(BigDecimal deductionAmount, BigDecimal discountRate) {
        this.deductionAmount = deductionAmount;
        this.discountRate = discountRate;
    }

    public int calculateFare(int totalFare) {
        BigDecimal inverseOfDiscountRate = ONE.subtract(discountRate);

        return BigDecimal.valueOf(totalFare)
            .subtract(deductionAmount)
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
