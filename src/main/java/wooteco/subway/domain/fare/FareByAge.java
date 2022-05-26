package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareByAge {

    ELDERLY(age -> age < 6 || age >= 65, fare -> 0),
    CHILD(age -> age >= 6 && age < 13, fare -> (fare - 350) / 2),
    TEENAGER(age -> age >= 13 && age < 18, fare -> (int) ((fare - 350) * 0.8)),
    GENERAL(age -> age >= 18 && age < 65, fare -> fare);

    private Predicate<Integer> predicate;
    private Function<Integer, Integer> function;

    FareByAge(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static Integer calculateFare(int age, int fare) {
        return Arrays.stream(FareByAge.values())
                .filter(it -> it.predicate.test(age))
                .findFirst()
                .map(it -> it.function.apply(fare))
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 찾을 수 없는 나이입니다."));
    }
}
