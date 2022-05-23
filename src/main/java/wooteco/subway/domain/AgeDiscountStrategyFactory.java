package wooteco.subway.domain;

public class AgeDiscountStrategyFactory {

    public static AgeDiscountStrategy from(int age) {
        if (age >= 6 && age < 13) {
            return new ChildrenDiscountStrategy();
        }

        if (age >= 13 && age < 19) {
            return new TeenagerDiscountStrategy();
        }

        return new DefaultDiscountStrategy();
    }
}
