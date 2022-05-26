package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public class AgeDecorator extends Decorator {

    private final int age;

    public AgeDecorator(final Fare fare, final int age) {
        super(fare);
        this.age = age;
    }

    @Override
    public double calculateExtraFare() {
        double price = super.calculateExtraFare();
        return AgeDiscountPolicy.valueOf(age, price);
    }

    private enum AgeDiscountPolicy {
        BABY(price -> 0D, AgeDiscountPolicy::isBaby),
        CHILDREN(price -> deduct(price) * 0.5, AgeDiscountPolicy::isChildren),
        TEENAGER(price -> deduct(price) * 0.8, AgeDiscountPolicy::isTeenager),
        NORMAL(price -> price, AgeDiscountPolicy::isNormal),
        ;



        private static final int BASIC_DEDUCT_PRICE = 350;
        private static final int BABY_UPPER_BOUND = 6;
        private static final int BABY_LOWER_BOUND = 0;
        private static final int TEENAGER_UPPER_BOUND = 19;
        private static final int TEENAGER_LOWER_BOUND = 13;
        private static final int CHILDREN_UPPER_BOUND = 13;
        private static final int CHILDREN_LOWER_BOUND = 6;

        private static boolean isBaby(final Integer age) {
            return BABY_LOWER_BOUND < age && age < BABY_UPPER_BOUND;
        }

        private static boolean isTeenager(final Integer age) {
            return TEENAGER_LOWER_BOUND <= age && age < TEENAGER_UPPER_BOUND;
        }

        private static boolean isChildren(final Integer age) {
            return CHILDREN_LOWER_BOUND <= age && age < CHILDREN_UPPER_BOUND;
        }

        private static boolean isNormal(final Integer age) {
            return age >= 19;
        }

        private static double deduct(final double price) {
            return price - BASIC_DEDUCT_PRICE;
        }

        private final Function<Double, Double> function;
        private final Predicate<Integer> predicate;

        AgeDiscountPolicy(final Function<Double, Double> function, final Predicate<Integer> predicate) {
            this.function = function;
            this.predicate = predicate;
        }

        private static double valueOf(int age, double price) {
            return Arrays.stream(values())
                    .filter(ageFare -> ageFare.predicate.test(age))
                    .map(ageFare -> ageFare.function.apply(price))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("해당 나이에 따른 할인율을 찾을수 없습니다."));
        }
    }
}
