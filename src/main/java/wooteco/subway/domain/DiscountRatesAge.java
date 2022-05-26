package wooteco.subway.domain;

import java.util.Arrays;

public enum DiscountRatesAge {
    ADULT(19, 0),
    TEENAGER(13, 0.2),
    CHILD(6, 0.5),
    BABY(0, 0.5)
    ;

    private final int startAge;
    private final double discountRate;

    DiscountRatesAge(int startAge, double discountRate) {
        this.startAge = startAge;
        this.discountRate = discountRate;
    }

    public static DiscountRatesAge from(int age) {
        return Arrays.stream(values())
                .filter(value -> value.startAge <= age)
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("적절한 연령이 입력되지 않았습니다."));
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
