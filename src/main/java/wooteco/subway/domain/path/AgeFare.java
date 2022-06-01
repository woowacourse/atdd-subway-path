package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeFare {
    KID(age -> age.isSameOrBiggerThan(6) && age.isSmallerThan(13),new Fare(350), 0.5),
    TEENAGER(age -> age.isSameOrBiggerThan(13) && age.isSmallerThan(19), new Fare(350), 0.2),
    FREE(age -> age.isSmallerThan(6) || age.isSameOrBiggerThan(65), new Fare(0), 1),
    PUBLIC(age -> age.isSameOrBiggerThan(19) && age.isSmallerThan(65), new Fare(0), 0)
    ;

    private final Predicate<Age> condition;
    private final Fare deduction;
    private final double discountRate;

    AgeFare(Predicate<Age> condition, Fare deduction, double discountRate) {
        this.condition = condition;
        this.deduction = deduction;
        this.discountRate = discountRate;
    }

    public static Fare calculate(Fare fare, Age age) {
        AgeFare applicableAgeFare = Arrays.stream(values())
                .filter(ageFare -> ageFare.condition.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 요금 정책을 찾을 수 없습니다."));
        return applicableAgeFare.calculate(fare);
    }

    private Fare calculate(Fare fare) {
        Fare deductedFare = fare.subtract(deduction);
        return deductedFare.discount(discountRate);
    }
}
