package wooteco.subway.domain.fare.farestrategy.discount;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.farestrategy.DiscountStrategy;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.ExceptionMessage;

public enum AgeStandard {
    ADULT(age -> 19 <= age, new AdultStrategy()),
    TEENAGER(age -> 13 <= age && age < 19, new TeenagerStrategy()),
    CHILD(age -> 6 <= age && age < 13, new ChildStrategy()),
    INFANT(age -> 0 < age && age < 6, new InfantStrategy());

    private final Predicate<Integer> agePredicate;
    private final DiscountStrategy discountStrategy;

    AgeStandard(Predicate<Integer> agePredicate,
                DiscountStrategy calculateFunction) {
        this.agePredicate = agePredicate;
        this.discountStrategy = calculateFunction;
    }

    public static DiscountStrategy findStrategy(int age) {
        return Arrays.stream(values())
                .filter(standard -> standard.agePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new DomainException(ExceptionMessage.INVALID_AGE.getContent()))
                .discountStrategy;
    }
}
