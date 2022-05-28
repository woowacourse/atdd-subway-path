package wooteco.subway.domain;

public class DiscountSpecification {

    private Age age;
    private int fare;

    public DiscountSpecification(Age age, int fare) {
        this.age = age;
        this.fare = fare;
    }

    public Age getAge() {
        return age;
    }

    public int getFare() {
        return fare;
    }
}
