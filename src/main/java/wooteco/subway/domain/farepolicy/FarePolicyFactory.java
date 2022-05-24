package wooteco.subway.domain.farepolicy;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class FarePolicyFactory {

    private static final Map<AgeRange, Supplier<FarePolicy>> cache;

    static {
        cache = new EnumMap<>(AgeRange.class);
        cache.put(AgeRange.EARLY_CHILDHOOD, PreferentialPolicy::new);
        cache.put(AgeRange.CHILDREN, ChildrenPolicy::new);
        cache.put(AgeRange.TEENAGER, TeenagerPolicy::new);
        cache.put(AgeRange.ADULT, BasicPolicy::new);
        cache.put(AgeRange.ELDER, PreferentialPolicy::new);
    }

    public static FarePolicy from(int age) {
        Supplier<FarePolicy> farePolicy = Optional.ofNullable(cache.get(AgeRange.from(age)))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 나이가 입력되었습니다."));
        return farePolicy.get();
    }
}
