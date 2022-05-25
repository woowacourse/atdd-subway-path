package wooteco.subway.domain.policy;

import java.util.List;
import wooteco.subway.domain.Path;

public class FarePolicies {

    private static final int BASE_FARE = 1250;

    private final List<FarePolicy> policies;

    FarePolicies(List<FarePolicy> policies) {
        this.policies = policies;
    }

    public static FarePolicies of(Path path, int age) {
        return new FarePolicies(List.of(
            new DistanceExtraFarePolicy(path.getDistance()),
            new LineExtraFarePolicy(path.getExtraFares()),
            new AgeDisCountFarePolicy(age)
        ));
    }

    public int calculate() {
        int fare = BASE_FARE;
        for (FarePolicy policy : policies) {
            fare = policy.apply(fare);
        }
        return fare;
    }
}
