package wooteco.subway.domain.fare;

public enum Distance {

    BASIC(0, 0, 9),
    MIDDLE(5, 10, 49),
    FAR(8, 50, Integer.MAX_VALUE);

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final int unit;
    private final int startPoint;
    private final int endPoint;

    Distance(int unit, int startPoint, int endPoint) {
        this.unit = unit;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public int calculateAdditionalFare(int distance) {
        if (distance <= startPoint) {
            return 0;
        }
        if (BASIC == this) {
            return BASIC_FARE;
        }
        if (distance > endPoint) {
            return calculate(endPoint);
        }
        return calculate(distance);
    }

    private int calculate(int distance) {
        return ((distance - startPoint - 1) / unit + 1) * EXTRA_FARE;
    }
}
