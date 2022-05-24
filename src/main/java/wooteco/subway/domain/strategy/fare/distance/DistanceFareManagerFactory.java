package wooteco.subway.domain.strategy.fare.distance;


import java.util.List;

public class DistanceFareManagerFactory {

    public static DistanceFareManager createDistanceFareManager() {
        return new DistanceFareManager(
                List.of(new FirstDistanceFareStrategy(),
                        new SecondDistanceFareStrategy(),
                        new ThirdDistanceFareStrategy()));
    }
}
