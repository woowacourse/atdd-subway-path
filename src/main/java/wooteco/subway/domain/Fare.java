package wooteco.subway.domain;

public class Fare {

    private final Distance distance;
    private final int extraFare;
    private final int age;
    private static final int MINIMUM_DISTANCE = 0;
    private static final int MINIMUM_EXTRA_FARE = 0;
    private static final int MINIMUM_AGE = 0;

    public Fare(final Distance distance, final int extraFare, final int age) {
        validateZeroOrPositiveDistance(extraFare, age);
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = age;
    }

    private void validateZeroOrPositiveDistance(int extraFare, int age) {
        if (extraFare < MINIMUM_EXTRA_FARE) {
            throw new IllegalArgumentException("추가요금은 음수일 수 없습니다.");
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
