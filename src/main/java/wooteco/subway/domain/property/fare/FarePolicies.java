package wooteco.subway.domain.property.fare;

import java.util.List;

import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.property.Age;

public class FarePolicies {

    private final List<FarePolicy> policies;

    private FarePolicies(List<FarePolicy> policies) {
        this.policies = policies;
    }

    public static FarePolicies of(Path path, Age age) {
        return new FarePolicies(List.of(
            new DistanceFarePolicy(path.getDistance()),
            new LineFarePolicy(path.getExtraFares()),
            new AgeFarePolicy(age)
        ));
    }

    public Fare calculate(Fare fare) {
        for (FarePolicy policy : policies) {
            fare = policy.apply(fare);
        }
        return fare;
    }
}
