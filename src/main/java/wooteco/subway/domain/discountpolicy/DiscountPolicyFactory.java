package wooteco.subway.domain.discountpolicy;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DiscountPolicyFactory {

    private static final Map<AgeRange, Supplier<DiscountPolicy>> cache;

    static {
        cache = new EnumMap<>(AgeRange.class);
        cache.put(AgeRange.EARLY_CHILDHOOD, PreferentialPolicy::new);
        cache.put(AgeRange.CHILDREN, ChildrenPolicy::new);
        cache.put(AgeRange.TEENAGER, TeenagerPolicy::new);
        cache.put(AgeRange.ADULT, BasicPolicy::new);
        cache.put(AgeRange.ELDER, PreferentialPolicy::new);
    }

    public static DiscountPolicy from(int age) {
        Supplier<DiscountPolicy> farePolicy = Optional.ofNullable(cache.get(AgeRange.from(age)))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 나이가 입력되었습니다."));
        return farePolicy.get();
    }
}
