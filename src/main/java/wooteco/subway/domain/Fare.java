package wooteco.subway.domain;

import java.util.List;

public enum Fare {

    AREA_0(1250, 1, 0, 10, 0),
    AREA_1(0, 5, 10, 50, 100),
    AREA_2(0, 8, 50, 10000, 100),
    ;

    private final int defaultFare;
    private final int standardDistance;
    private final int overLimitOfDistance;
    private final int underLimitOfDistance;
    private final int additionalFare;

    Fare(int defaultFare, int standardDistance, int overLimitOfDistance, int underLimitOfDistance, int additionalFare) {
        this.defaultFare = defaultFare;
        this.standardDistance = standardDistance;
        this.overLimitOfDistance = overLimitOfDistance;
        this.underLimitOfDistance = underLimitOfDistance;
        this.additionalFare = additionalFare;
    }

    public static int calculate(final Sections sections) {
        return getMaxExtraFare(sections.getLines()) + calculate(sections.getTotalDistance());
    }

    private static int getMaxExtraFare(final List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    private static int calculate(final double distance) {
        return AREA_0.defaultFare + calculateMoreFare(distance, AREA_1) + calculateMoreFare(distance, AREA_2);
    }

    private static int calculateMoreFare(final double distance, final Fare fare) {
        if (distance <= fare.overLimitOfDistance) {
            return fare.defaultFare;
        }
        int portion = getPortion(calculateDistance(distance, fare), fare.standardDistance);
        return portion * fare.additionalFare;
    }

    private static double calculateDistance(final double distance, Fare fare) {
        if (distance >= fare.underLimitOfDistance) {
            return fare.underLimitOfDistance - fare.overLimitOfDistance;
        }
        return distance - fare.overLimitOfDistance;
    }

    private static int getPortion(final double distance, final int standardDistance) {
        final int portion = (int) distance / standardDistance;
        if (portion == 0) {
            return 1;
        }
        return portion;
    }
}
