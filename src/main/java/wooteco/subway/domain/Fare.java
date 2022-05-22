package wooteco.subway.domain;

public class Fare {
    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int LEVEL_ONE_ADDITIONAL_DISTANCE = 50;
    private static final int ADDITIONAL_BASIC_FARE = 800;

    private final int price;
    private final int age;

    public Fare(final int price, final int age) {
        this.price = price;
        this.age = age;
    }

    public int getPrice() {
        return price;
    }

    public static int calculateFare(int distance, int extraFare) {
        if (distance < BASIC_DISTANCE) {
            return BASIC_FARE + extraFare;
        }

        if (distance < LEVEL_ONE_ADDITIONAL_DISTANCE) {
            return BASIC_FARE + extraFare + (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100);
        }

        return BASIC_FARE + extraFare + ADDITIONAL_BASIC_FARE + (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100);
    }
}
