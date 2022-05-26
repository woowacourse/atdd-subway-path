package wooteco.subway.domain.property.fare;

import java.util.List;

import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.property.Age;

public class FarePolicies {

    private final List<FarePolicy> policies;

    private FarePolicies(List<FarePolicy> policies) {
        this.policies = policies;
    }

    public static FarePolicies of(PathFinder pathFinder, Age age) {
        return new FarePolicies(
            List.of(
                new DistanceFarePolicy(pathFinder.getDistance()),
                new LineFarePolicy(pathFinder.getPassingFares()),
                new AgeFarePolicy(age)
            )
        );
    }

    public int applyAll(int fare) {
        for (FarePolicy policy : policies) {
            fare = policy.apply(fare);
        }
        return fare;
    }

    public List<FarePolicy> getPolicies() {
        return List.copyOf(policies);
    }
}
