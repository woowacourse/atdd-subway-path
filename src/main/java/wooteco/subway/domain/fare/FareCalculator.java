package wooteco.subway.domain.fare;

import java.util.List;
import wooteco.subway.domain.line.Lines;

public class FareCalculator {

    private final List<Long> lineIds;

    public FareCalculator(final List<Long> lineIds) {
        this.lineIds = lineIds;
    }

    public int calculateFare(final int age, final double distance, final Lines lines) {
        int maxExtraFare = lines.findMaxExtraFare(lineIds);
        return calculateFare(age, distance, maxExtraFare);
    }

    private int calculateFare(final int age, final double distance, final int maxExtraFare) {
        int distanceFare = FareByDistance.calculateFare(distance);
        return FareByAge.calculateFare(age, distanceFare + maxExtraFare);
    }

}
