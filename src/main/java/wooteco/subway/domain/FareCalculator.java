package wooteco.subway.domain;

public class FareCalculator {
    private static final int MINIMUM_FARE = 1250;

    public int execute(int distanceFrom) {
        return MINIMUM_FARE + calculateOver50km(distanceFrom) + calculateOver10kmUnder50km(distanceFrom);
    }

    private int calculateOver50km(int distance) {
        int i = distance - 50;
        if (i < 0) {
            return 0;
        }
        return (((i-1) / 8) + 1) * 100 + calculateOver10kmUnder50km(50);
    }

    private int calculateOver10kmUnder50km(int distance) {
        int i = distance - 10;
        if (i < 0 || i > 40) {
            return 0;
        }
        return (((i-1) / 5) + 1) * 100;
    }
}
