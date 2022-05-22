package wooteco.subway.domain;

import java.util.Arrays;

public enum Fare {

    BASIC(9, 0, 0, 0),
    FIRST_SECTION(50, 10, 5, 1250),
    SECOND_SECTION(51, 50, 8, 2050);

    private static final int BASIC_FARE = 1250;

    private final int section;
    private final int basicSection;
    private final int unit;
    private final int previousSectionFare;

    Fare(int section, int basicSection, int unit, int previousSectionFare) {
        this.section = section;
        this.basicSection = basicSection;
        this.unit = unit;
        this.previousSectionFare = previousSectionFare;
    }

    public static Fare find(int distance) {
        return Arrays.stream(Fare.values())
                .filter(fare -> distance - fare.section <= 0)
                .findFirst()
                .orElse(SECOND_SECTION);
    }

    public int calc(int distance) {
        if (this == BASIC) {
            return BASIC_FARE;
        }
        distance -= basicSection;
        int fare = 0;
        fare += (distance / unit) * 100;
        if (distance % unit > 0 || (distance / unit == 0)) {
            fare += 100;
        }
        return previousSectionFare + fare;
    }
}
