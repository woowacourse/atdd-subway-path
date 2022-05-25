package wooteco.subway.domain.farepolicy;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class FarePolicyFactory {

    private static final Map<DistanceRange, Supplier<FarePolicy>> cache;

    static {
        cache = new EnumMap<>(DistanceRange.class);
        cache.put(DistanceRange.BASIC_DISTANCE, BasicRangePolicy::new);
        cache.put(DistanceRange.SHORT_RANGE, ShortRangePolicy::new);
        cache.put(DistanceRange.LONG_RANGE, LongRangePolicy::new);
    }

    public static FarePolicy from(int distance) {
        Supplier<FarePolicy> farePolicy = Optional.ofNullable(cache.get(DistanceRange.from(distance)))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 거리가 입력되었습니다."));
        return farePolicy.get();
    }
}
