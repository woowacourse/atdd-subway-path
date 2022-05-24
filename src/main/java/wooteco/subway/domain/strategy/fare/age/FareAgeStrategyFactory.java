package wooteco.subway.domain.strategy.fare.age;

import java.util.List;

public class FareAgeStrategyFactory {

    public static FareAgeStrategyManager createAgeStrategy() {
        return new FareAgeStrategyManager(List.of(
                new InfantStrategy(),
                new ChildStrategy(),
                new TeenAgeStrategy(),
                new AdultStrategy()));
    }
}
