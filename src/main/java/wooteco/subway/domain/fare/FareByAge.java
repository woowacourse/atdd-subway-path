package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.exception.PositiveDigitException;

public enum FareByAge {

    FREE_AGE(age -> age >= 65 || age <= 5, fare -> 0),
    CHILDREN(age -> age > 5 && age < 13, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(age -> age > 12 && age < 19, fare -> (int) ((fare - 350) * 0.8)),
    ADULT(age -> 18 > age, fare -> fare);

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    FareByAge(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int findFare(final int age, final int fare) {
        return Arrays.stream(values())
                .filter(fareByAge -> fareByAge.predicate.test(age))
                .findFirst()
                .orElseThrow(() -> new PositiveDigitException("나이는 음수일 수 없습니다."))
                .function
                .apply(fare);
    }

}
