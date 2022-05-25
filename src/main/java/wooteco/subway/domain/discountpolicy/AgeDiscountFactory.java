package wooteco.subway.domain.discountpolicy;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AgeDiscountFactory {

    private static final Map<AgeRange, Supplier<AgeDiscountPolicy>> cache = AgeRange.generateCache();

    public static AgeDiscountPolicy from(final AgeRange ageRange) {
        final Supplier<AgeDiscountPolicy> supplier = Optional.ofNullable(cache.get(ageRange))
                .orElseThrow(IllegalAccessError::new);
        return supplier.get();
    }
}
