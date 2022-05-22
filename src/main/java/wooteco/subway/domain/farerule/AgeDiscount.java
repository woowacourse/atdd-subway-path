package wooteco.subway.domain.farerule;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscount implements Predicate<Integer> {

    BABY(age -> age < 6, fare -> fare),
    CHILD(age -> age >= 6 && age < 13, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(age -> age >= 13 && age < 19, fare -> (int) ((fare - 350) * 0.2)),
    ADULT(age -> age >= 19, fare -> 0);

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> calculateDiscount;

    AgeDiscount(Predicate<Integer> predicate, Function<Integer, Integer> calculateDiscount) {
        this.predicate = predicate;
        this.calculateDiscount = calculateDiscount;
    }

    public int calculate(int fare) {
        return this.calculateDiscount.apply(fare);
    }

    public static AgeDiscount findAgeDiscount(int age) {
        return Arrays.stream(AgeDiscount.values())
                .filter(type -> type.test(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean test(Integer age) {
        return predicate.test(age);
    }
}
