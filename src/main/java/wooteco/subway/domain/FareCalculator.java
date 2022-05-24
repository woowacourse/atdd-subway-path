package wooteco.subway.domain;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;

    private final int distance;
    private final int extraFare;
    private final Age age;

    public FareCalculator(int distance, int extraFare, int age) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = Age.findByAge(age);
    }

    public int calculate() {
        return age.calculateFare(calculateByDistance() + extraFare);
    }

    private int calculateByDistance() {
        int userDistance = this.distance;
        int sum = BASIC_FARE;

        for (Distance distance : Distance.sortedByDistanceUnit()) {
            sum += distance.calculate(userDistance);
            userDistance = distance.getDistanceUnit();
        }
        return sum;
    }
}
