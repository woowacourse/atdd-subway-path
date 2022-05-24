package wooteco.subway.domain.discount.implement;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.DiscountSpecification;
import wooteco.subway.domain.discount.DiscountStrategy;

@Component
@Qualifier("Age")
public class DiscountByAge implements DiscountStrategy {

    @Override
    public int discount(DiscountSpecification specification) {
        return DiscountStrategyByAge.getDiscountFare(specification.getAge(), specification.getFare());
    }
}
