package wooteco.subway.domain;

public class DiscountSpecification {

    private int age;
    private int fare;

    public DiscountSpecification(int age, int fare) {
        this.age = age;
        this.fare = fare;
    }

    public int getAge() {
        return age;
    }

    public int getFare() {
        return fare;
    }
}
