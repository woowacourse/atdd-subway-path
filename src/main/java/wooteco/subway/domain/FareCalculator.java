package wooteco.subway.domain;

public class FareCalculator {
    private static final int MINIMUM_FARE = 1250;

    public int execute(double distanceFrom) {
        return MINIMUM_FARE + calculateOver50km(distanceFrom) + calculateOver10kmUnder50km(distanceFrom);
    }

    private int calculateOver50km(double distance) {
        double extraDistance = distance - 50;
        if (extraDistance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil(extraDistance / 8)) * 100) + calculateOver10kmUnder50km(50);
    }

    private int calculateOver10kmUnder50km(double distance) {
        double extraDistance = distance - 10;
        if (extraDistance <= 0 || extraDistance > 40) {
            return 0;
        }
        return (int) ((Math.ceil(extraDistance / 5)) * 100);
    }
}