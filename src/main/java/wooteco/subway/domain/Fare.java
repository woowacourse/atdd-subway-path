package wooteco.subway.domain;

import java.util.Optional;

public class Fare {

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
