package wooteco.subway.domain;

public class Fare {
    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int LEVEL_ONE_ADDITIONAL_DISTANCE = 50;
    private static final int ADDITIONAL_BASIC_FARE = 800;
    private static final int DEDUCTIBLE_AMOUNT = 350;

    private final int price;

    public Fare(final int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public double calculateFare(int distance, int age) {
        if (distance < BASIC_DISTANCE) {
            return new Fare(BASIC_FARE + price).applyDiscount(age);
        }

        if (distance < LEVEL_ONE_ADDITIONAL_DISTANCE) {
            return new Fare(BASIC_FARE + price + (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100)).applyDiscount(age);
        }

        return new Fare(BASIC_FARE + price + ADDITIONAL_BASIC_FARE + (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100)).applyDiscount(age);
    }

    public double applyDiscount(final int age) {
        int deductedPrice = price - DEDUCTIBLE_AMOUNT;
        if (age >= 13 && age < 19) {
            return 0.8 * deductedPrice;
        }
        if (age >= 6 && age < 13) {
            return 0.5 * deductedPrice;
        }
        return price;
    }
}
