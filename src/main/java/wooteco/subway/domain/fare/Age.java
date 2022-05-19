package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Age {
    BABY(age -> age < 6, fare -> 0),
    CHILD(age -> 6 <= age && age < 13, fare -> (fare - 350) / 2),
    TEENAGER(age -> 13 <= age && age < 19, fare -> (fare - 350) * 4 / 5),
    ADULT(age -> 19 <= age, fare -> fare);

    private final Predicate<Integer> condition;
    private final Function<Integer, Integer> discount;

    Age(Predicate<Integer> condition, Function<Integer, Integer> discountBy) {
        this.condition = condition;
        this.discount = discountBy;
    }

    public static int discountByAge(int age, int baseFare) {
        return Arrays.stream(Age.values())
                .filter(type -> type.condition.test(age))
                .map(type -> type.discount.apply(baseFare))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 연령대가 없습니다."));
    }
}
