package wooteco.subway.domain.fare;

public class FareCondition {

    private final int distance;
    private final int age;
    private final int extraFare;

    public FareCondition(final int distance, final int age, final int extraFare) {
        this.distance = distance;
        this.age = age;
        this.extraFare = extraFare;
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
