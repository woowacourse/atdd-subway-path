package wooteco.subway.domain.discountpolicy;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum AgeRange {

    PRESCHOOLER(1, 5, PreschoolerPolicy::new),
    CHILD(6, 12, ChildDiscountPolicy::new),
    TEEN(13, 18, TeenDiscountPolicy::new),
    ADULT(19, Integer.MAX_VALUE, AdultDiscountPolicy::new)
    ;

    private final int minRange;
    private final int maxRage;
    private final Supplier<AgeDiscountPolicy> policySupplier;

    AgeRange(final int minRange, final int maxRage, final Supplier<AgeDiscountPolicy> policySupplier) {
        this.minRange = minRange;
        this.maxRage = maxRage;
        this.policySupplier = policySupplier;
    }

    public static AgeRange from(final int targetAge) {
        return Arrays.stream(values())
                .filter(age -> age.minRange <= targetAge && targetAge <= age.maxRage)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Map<AgeRange, Supplier<AgeDiscountPolicy>> generateCache() {
        return Arrays.stream(values())
                .map(age -> Map.entry(age, age.policySupplier))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
