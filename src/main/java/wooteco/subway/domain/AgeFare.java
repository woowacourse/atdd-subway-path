package wooteco.subway.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeFare {
    CHILDREN((price) -> deduct(price) * 0.5, (age) -> age >= 6 && age < 13),
    TEENAGER((price) -> deduct(price) * 0.8, (age) -> age >= 13 && age < 19),
    NORMAL(Double::valueOf, ignore -> true),
    ;

    private final Function<Integer, Double> function;
    private final Predicate<Integer> predicate;

    private static int deduct(final Integer price) {
        return price - 350;
    }

    AgeFare(final Function<Integer, Double> function, final Predicate<Integer> predicate) {
        this.function = function;
        this.predicate = predicate;
    }

    public static double valueOf(int age, int price) {
        return Arrays.stream(values())
                .filter(ageFare -> ageFare.predicate.test(age))
                .map(ageFare -> ageFare.function.apply(price))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 나이에 따른 할인율을 찾을수 없습니다."));
    }
}
