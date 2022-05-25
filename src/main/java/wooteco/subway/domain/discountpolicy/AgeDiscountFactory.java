package wooteco.subway.domain.discountpolicy;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AgeDiscountFactory {

    private static final Map<Age, Supplier<AgeDiscountPolicy>> cache = Age.generateCache();

    public static AgeDiscountPolicy from(final int age) {
        final Supplier<AgeDiscountPolicy> supplier = Optional.ofNullable(cache.get(Age.from(age)))
                .orElseThrow(IllegalAccessError::new);
        return supplier.get();
    }
}
