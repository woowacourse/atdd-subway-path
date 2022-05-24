package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareByAge {
    FREE(age -> age < 6 || age >= 65, totalFare -> 0.0),
    CHILD(age -> age >= 6 && age <= 12, totalFare -> (totalFare - 350) * 0.5),
    TEENAGER(age -> age >= 13 && age <= 19, totalFare -> (totalFare - 350) * 0.8),
    ADULT(age -> age >= 20 && age <= 64, totalFare -> totalFare * 1.0);

    private Predicate<Integer> predicate;
    private Function<Integer, Double> function;

    FareByAge(Predicate<Integer> predicate, Function<Integer, Double> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static Integer calculateFareByAge(int age, int totalFare) {
        return (int) Math.ceil(Arrays.stream(FareByAge.values())
                .filter(it -> it.predicate.test(age))
                .findFirst()
                .map(it -> it.function.apply(totalFare))
                .orElseThrow());
    }
}
