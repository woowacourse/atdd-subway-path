package wooteco.subway.domain.strategy.fare.discount;

import java.util.List;

public class DiscountManagerFactory {

    public static DiscountManager createDiscountManager() {
        return new DiscountManager(List.of(
                new ChildDiscountStrategy(),
                new TeenagerDiscountStrategy()
        ));
    }
}
