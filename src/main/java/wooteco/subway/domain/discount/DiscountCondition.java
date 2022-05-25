package wooteco.subway.domain.discount;

public class DiscountCondition {

    private final int age;
    private final int distance;

    public DiscountCondition(final int age, final int distance) {
        this.age = age;
        this.distance = distance;
    }

    public int getAge() {
        return age;
    }

    public int getDistance() {
        return distance;
    }
}
