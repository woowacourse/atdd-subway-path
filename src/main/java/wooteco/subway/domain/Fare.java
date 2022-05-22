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

    Fare(final int defaultFare, final int standardDistance, final int overLimitOfDistance, final int underLimitOfDistance, final int additionalFare) {
        this.defaultFare = defaultFare;
        this.standardDistance = standardDistance;
        this.overLimitOfDistance = overLimitOfDistance;
        this.underLimitOfDistance = underLimitOfDistance;
        this.additionalFare = additionalFare;
    }

    public static int calculate(final Sections sections, final Age age) {
        final int fare = getMaxExtraFare(sections.getLines()) + calculateByArea(sections.getTotalDistance());
        return age.discountFare(fare);
    }

    private static int getMaxExtraFare(final List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    private static int calculateByArea(final double distance) {
        return AREA_0.defaultFare + calculateMoreFare(distance, AREA_1) + calculateMoreFare(distance, AREA_2);
    }

    private static int calculateMoreFare(final double distance, final Fare fare) {
        if (distance <= fare.overLimitOfDistance) {
            return fare.defaultFare;
        }
        int portion = getPortion(calculateDistance(distance, fare), fare.standardDistance);
        return portion * fare.additionalFare;
    }

    private static double calculateDistance(final double distance, final Fare fare) {
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
