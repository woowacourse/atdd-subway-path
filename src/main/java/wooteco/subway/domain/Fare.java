package wooteco.subway.domain;

import java.util.Arrays;

public enum Fare {

    BASIC(9, 0, 0, 0),
    FIRST_SECTION(50, 10, 5, 1250),
    SECOND_SECTION(51, 50, 8, 1250 + 800);

    private static final int BASIC_FARE = 1250;

    private final int section;
    private final int previousSection;
    private final int unit;
    private final int previousSectionFare;

    Fare(int section, int previousSection, int unit, int previousSectionFare) {
        this.section = section;
        this.previousSection = previousSection;
        this.unit = unit;
        this.previousSectionFare = previousSectionFare;
    }

    public static int calculateFare(final int distance) {
        Fare fare = find(distance);
        return fare.calc(distance);
    }
    private static Fare find(final int distance) {
        return Arrays.stream(Fare.values())
                .filter(fare -> distance - fare.section <= 0)
                .findFirst()
                .orElse(SECOND_SECTION);
    }

    private int calc(final int distance) {
        int calcDistance = distance;
        if (this == BASIC) {
            return BASIC_FARE;
        }
        calcDistance -= previousSection;
        int fare = 0;
        fare += (calcDistance / unit) * 100;
        if (calcDistance % unit > 0 || (calcDistance / unit == 0)) {
            fare += 100;
        }
        return previousSectionFare + fare;
    }
}
