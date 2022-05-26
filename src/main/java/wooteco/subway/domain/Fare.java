package wooteco.subway.domain;

public class Fare {

    private final int distance;
    private final int extraFare;
    private final int age;
    private static final int MINIMUM_DISTANCE = 0;
    private static final int MINIMUM_EXTRA_FARE = 0;
    private static final int MINIMUM_AGE = 0;

    public Fare(final int distance, final int extraFare, final int age) {
        validateZeroOrPositiveDistance(distance, extraFare, age);
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = age;
    }

    private void validateZeroOrPositiveDistance(int distance, int extraFare, int age) {
        if (distance <= MINIMUM_DISTANCE || extraFare < MINIMUM_EXTRA_FARE) {
            throw new IllegalArgumentException("거리 또는 추가요금은 음수일 수 없습니다.");
        }
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException("나이는 음수일 수 없습니다.");
        }
    }

    public int calculateFare() {
        FareAgeStrategy ageStrategy = FareAgeStrategy.of(age);
        return ageStrategy.calculate(FareRangeStrategy.getFare(distance)) + extraFare;
    }

}
