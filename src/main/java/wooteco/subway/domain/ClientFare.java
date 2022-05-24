package wooteco.subway.domain;

import wooteco.subway.domain.farediscount.DiscountPolicy;

public class ClientFare {

    private final int age;
    private final int distance;
    private final Sections sections;

    public ClientFare(int age, Sections sections, int distance) {
        this.age = age;
        this.distance = distance;
        this.sections = sections;
    }

    public int calculateFare() {
        Fare fare = new Fare();
        int chargePrice = sections.findChargePrice();
        int price = fare.calculate(distance) + chargePrice;

        DiscountPolicy discountPolicy = DiscountFactory.findDiscountPolicy(age);
        return discountPolicy.apply(price);
    }
}
