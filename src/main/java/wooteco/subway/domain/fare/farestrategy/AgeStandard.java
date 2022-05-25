package wooteco.subway.domain.fare.farestrategy;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.NotFoundException;

public enum AgeStandard {
    ADULT(age -> 19 <= age, Function.identity()),
    TEENAGER(age -> 13 <= age && age < 19, x -> (long) ((x - 350) * 0.8)),
    CHILD(age -> 6 <= age && age < 13, x -> (long) ((x - 350) * 0.5)),
    INFANT(age -> 0 < age && age < 6, x -> 0L);

    private final Predicate<Integer> agePredicate;
    private final Function<Long, Long> calculateFunction;

    AgeStandard(Predicate<Integer> agePredicate,
                Function<Long, Long> calculateFunction) {
        this.agePredicate = agePredicate;
        this.calculateFunction = calculateFunction;
    }

    public static AgeStandard from(int age) {
        return Arrays.stream(values())
                .filter(standard -> standard.agePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.INVALID_AGE.getContent()));
    }

    public long calculate(long amount) {
        return calculateFunction.apply(amount);
    }
}
