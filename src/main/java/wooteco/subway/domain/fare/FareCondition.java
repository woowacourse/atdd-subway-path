package wooteco.subway.domain.fare;

public class FareCondition {

    private final int distance;
    private final int age;
    private final int extraFare;

    public FareCondition(int distance, int age, int extraFare) {
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
