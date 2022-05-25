package wooteco.subway.domain;

import java.util.List;

public class Fare {

    private static final int DEFAULT_EXTRA_FARE = 0;

    public static int chargeFare(Path path, int age) {
        double distance = path.calculateShortestDistance();
        int fare = ExtraFarePolicy.findFare(distance);

        return calculateFareByAge(age, fare + findExtraLineFare(path));
    }

    private static int calculateFareByAge(int age, int fare) {
        return DiscountPolicy.calculateFareByAge(age, fare);
    }

    private static int findExtraLineFare(Path path) {
        List<Line> lines = path.findLineOnPath();

        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(DEFAULT_EXTRA_FARE);
    }
}
