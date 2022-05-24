package wooteco.subway.domain.fare;

import java.util.Arrays;

public enum AgeDiscountMapper {
    INFANT(0, 5, SpecialDiscountStrategy.getInstance()),
    CHILD(6, 12, ChildDiscountStrategy.getInstance()),
    ADOLESCENT(13, 18, AdolescentDiscountStrategy.getInstance()),
    ADULT(19, 64, DefaultDiscountStrategy.getInstance()),
    ELDER(65, Integer.MAX_VALUE, SpecialDiscountStrategy.getInstance());

    private final int minAge;
    private final int maxAge;
    private final DiscountStrategy discountStrategy;

    AgeDiscountMapper(int minAge, int maxAge, DiscountStrategy discountStrategy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountStrategy = discountStrategy;
    }

    public static int discount(int age, int fare) {
        return Arrays.stream(values())
                .filter(it -> age >= it.minAge && age <= it.maxAge)
                .map(it -> it.discountStrategy.discount(fare))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올 수 없는 나이입니다."));
    }
}
