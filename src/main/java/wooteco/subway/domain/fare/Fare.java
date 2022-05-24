package wooteco.subway.domain.fare;

import java.util.List;
import wooteco.subway.domain.fare.policy.FarePolicy;

public class Fare {
    private final List<FarePolicy> policies;
    private final int baseFare;

    public Fare(List<FarePolicy> policies, int baseFare) {
        this.policies = policies;
        this.baseFare = baseFare;
    }

    public int getFare() {
        double result = baseFare;
        for (FarePolicy policy : policies) {
            result = policy.calculate(result);
        }
        return (int) result;
    }
}
