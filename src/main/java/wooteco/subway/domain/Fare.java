package wooteco.subway.domain;

import java.util.List;
import java.util.Optional;

public class Fare {

    public static int chargeFare(Path path, List<Line> lines, List<Station> stations) {
        double distance = path.calculateShortestDistance();
        int fare = 0;
        Optional<FarePolicy> fareStandard = Optional.of(FarePolicy.DEFAULT);

        while (fareStandard.isPresent()) {
            FarePolicy presentStandard = fareStandard.get();
            fare += presentStandard.calculate(distance);
            fareStandard = presentStandard.update();
        }
        return fare + findExtraLineFare(lines, stations);
    }

    private static int findExtraLineFare(List<Line> allLines, List<Station> stations) {
        return allLines.get(0).getExtraFare();
    }

    public static int chargeFare(Path path) {
        double distance = path.calculateShortestDistance();
        int fare = 0;
        Optional<FarePolicy> fareStandard = Optional.of(FarePolicy.DEFAULT);

        while (fareStandard.isPresent()) {
            FarePolicy presentStandard = fareStandard.get();
            fare += presentStandard.calculate(distance);
            fareStandard = presentStandard.update();
        }
        return fare;
    }
}
