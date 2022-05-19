package wooteco.subway.domain;

public enum ExtraFare {
    OVER_TEN_KM(10, 5, 100),
    OVER_FIFTY_KM(50, 8, 100)
    ;

    private static final int BASIC_FARE = 1250;

    private final int criteria;
    private final int unit;
    private final int amount;

    ExtraFare(int criteria, int unit, int amount) {
        this.criteria = criteria;
        this.unit = unit;
        this.amount = amount;
    }

    public static int calculateTotalFare(double distance) {
        return BASIC_FARE + calculateTotalExtra(distance);
    }

    private static int calculateTotalExtra(double distance) {
        if (distance > OVER_FIFTY_KM.criteria) {
            return OVER_FIFTY_KM.calculate(distance - OVER_FIFTY_KM.criteria)
                    + OVER_TEN_KM.calculate(OVER_FIFTY_KM.criteria - OVER_TEN_KM.criteria);
        }
        if (distance > OVER_TEN_KM.criteria) {
            return OVER_TEN_KM.calculate(distance - OVER_TEN_KM.criteria);
        }
        return 0;
    }

    private int calculate(double distance) {
        return (int) ((Math.ceil(distance / unit)) * amount);
    }
}
