package wooteco.subway.domain;

public class FareCalculator {

    private static final int BASE_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int PRIMARY_BASIC_DISTANCE = 10;
    private static final int SECONDARY_BASIC_DISTANCE = 50;

    public int calculateFare(int distance) {
        return BASE_FARE + additionalFare(distance);
    }

    private int additionalFare(int distance) {
        return ADDITIONAL_FARE * additionalFareCount(distance);
    }

    private int additionalFareCount(int distance) {
        if (distance > SECONDARY_BASIC_DISTANCE) {
            return additionalFareCountOverSecondBasicDistance(distance);
        }

        if (distance > PRIMARY_BASIC_DISTANCE) {
            return additionalFareCountOverFirstBasicDistance(distance);
        }

        return 0;
    }

    private int additionalFareCountOverSecondBasicDistance(int distance) {
        int remain = distance - 50;
        int count = remain / 8;
        if (remain % 8 != 0) {
            count++;
        }
        return count + 8;
    }

    private int additionalFareCountOverFirstBasicDistance(int distance) {
        int remain = distance - 10;
        int count = remain / 5;
        if (remain % 5 != 0) {
            count++;
        }
        return count;
    }
}