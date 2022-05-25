package wooteco.subway.domain.discountpolicy;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AgeDiscountFactory {

    private static final Map<AgeRange, Supplier<AgeDiscountPolicy>> cache = AgeRange.generateCache();

    public static AgeDiscountPolicy from(final int age) {
        final Supplier<AgeDiscountPolicy> supplier = Optional.ofNullable(cache.get(AgeRange.from(age)))
                .orElseThrow(IllegalAccessError::new);
        return supplier.get();
    }
}
