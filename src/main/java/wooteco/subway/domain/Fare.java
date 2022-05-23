package wooteco.subway.domain;

import java.util.Optional;

public class Fare {

    public static int chargeFare(Path path) {
        double distance = path.calculateShortestDistance();
        int fare = 0;
        Optional<FareStandard> fareStandard = Optional.of(FareStandard.DEFAULT);

        while (fareStandard.isPresent()) {
            FareStandard presentStandard = fareStandard.get();
            fare += presentStandard.calculate(distance);
            fareStandard = presentStandard.update();
        }
        return fare;
    }
}
