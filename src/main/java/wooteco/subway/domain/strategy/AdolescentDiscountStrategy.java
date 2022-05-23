package wooteco.subway.domain.strategy;

public class AdolescentDiscountStrategy implements DiscountStrategy {

    private static final int DEDUCTION = 350;

    @Override
    public int calculate(int fare) {
        int targetFare = fare - DEDUCTION;
        return fare - (int) (targetFare * 0.2);
    }
}
