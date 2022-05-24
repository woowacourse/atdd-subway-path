package wooteco.subway.domain.strategy.fare.distance;

import java.util.List;

public class FareDistanceStrategyFactory {

    public static FareDistanceStrategyManager createDistanceStrategy() {
        return new FareDistanceStrategyManager(List.of(
                new DefaultStrategy(),
                new Over10KmStrategy(),
                new Over50KmStrategy()
        ));
    }
}
