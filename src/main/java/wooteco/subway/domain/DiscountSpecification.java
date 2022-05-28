package wooteco.subway.domain;

public class DiscountSpecification {

    private Age age;
    private Fare fare;

    public DiscountSpecification(Age age, Fare fare) {
        this.age = age;
        this.fare = fare;
    }

    public Age getAge() {
        return age;
    }

    public Fare getFare() {
        return fare;
    }
}
