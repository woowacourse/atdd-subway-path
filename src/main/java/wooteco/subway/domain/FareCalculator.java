package wooteco.subway.domain;

public class FareCalculator {

    public static final int BASE_FARE = 1250;
    public static final int ADDITIONAL_FARE = 100;

    public int findFare(int distance) {
        return BASE_FARE + additionalFare(distance);
    }

    private int additionalFare(int distance) {
        return ADDITIONAL_FARE * additionalFareCount(distance);
    }

    private int additionalFareCount(int distance) {
        if (distance > 50) {
            return additionalFareCountOver50km(distance);
        }

        if (distance > 10) {
            return additionalFareCountOver10km(distance);
        }

        return 0;
    }

    private int additionalFareCountOver50km(int distance) {
        int remain = distance - 50;
        int count = remain / 8;
        if (remain % 8 != 0) {
            count++;
        }
        return count + additionalFareCountOver10km(distance - remain);
    }

    private int additionalFareCountOver10km(int distance) {
        int remain = distance - 10;
        int count = remain / 5;
        if (remain % 5 != 0) {
            count++;
        }
        return count;
    }
}