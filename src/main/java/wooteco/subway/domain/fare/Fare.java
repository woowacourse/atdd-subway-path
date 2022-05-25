package wooteco.subway.domain.fare;

public class Fare {

    private final int distance;
    private final int age;
    private final int extraFare;

    public Fare(int distance, int age, int extraFare) {
        this.distance = distance;
        this.age = age;
        this.extraFare = extraFare;
    }

    public int calculateFare() {
        DistanceProportionCalculator distanceProportionCalculator = DistanceProportionCalculator.from(distance);
        int fare = distanceProportionCalculator.calculateFare(distance);
        AgeDiscounter ageDiscounter = AgeDiscounter.from(age);
        return ageDiscounter.discount(fare + extraFare);
    }
}
