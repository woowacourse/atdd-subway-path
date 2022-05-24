package wooteco.subway.domain.strategy.fare.discount;

import java.util.List;

public class DiscountManager {
    private final List<DiscountStrategy> discountStrategies;

    public DiscountManager(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }

    public int calculateDiscount(int age, int price) {
        for (DiscountStrategy discountStrategy : discountStrategies) {
            if (discountStrategy.isUsable(age)) {
                return discountStrategy.calculateDiscount(price);
            }
        }
        return 0;
    }
}
