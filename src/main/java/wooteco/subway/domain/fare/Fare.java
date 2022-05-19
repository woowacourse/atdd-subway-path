package wooteco.subway.domain.fare;

public class Fare {
    private static final int BASE_FAIR = 1250;

    private final int distance;
    private final int age;

    public Fare(int distance, int age) {
        this.distance = distance;
        this.age = age;
    }

    public int calculateFare() {
        return Age.discountByAge(age, calculateFareByDistance());
    }

    private int calculateFareByDistance() {
        return BASE_FAIR
                + calculateFirstExtraFare(distance - 10)
                + calculateSecondExtraFare(distance - 50);
    }

    private int calculateFirstExtraFare(int distanceInFirstRange) {
        if (distanceInFirstRange <= 0) {
            return 0;
        }
        return ((distanceInFirstRange - 1) / 5 + 1) * 100;
    }

    private int calculateSecondExtraFare(int distanceInSecondRange) {
        if (distanceInSecondRange <= 0) {
            return 0;
        }
        return ((distanceInSecondRange - 1) / 8 + 1) * 100;
    }
}


