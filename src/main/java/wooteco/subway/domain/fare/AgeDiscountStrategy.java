package wooteco.subway.domain.fare;

public class AgeDiscountStrategy implements DiscountStrategy {
    @Override
    public int discount(int totalFare, int age) {
        validateDiscount(totalFare);
        int fare = totalFare;
        if (age >= 19) {
            return fare;
        }

        fare -= 350;
        if (age >= 13) {
            return fare * 8 / 10;
        }

        if (age >= 6) {
            return fare / 2;
        }
        
        return 0;
    }

    private void validateDiscount(int totalFare) {
        if (totalFare < 350) {
            throw new IllegalArgumentException("할인할 수 없는 금액입니다.");
        }
    }
}
