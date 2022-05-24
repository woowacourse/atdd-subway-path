package wooteco.subway.domain.fare;

import java.util.List;
import wooteco.subway.domain.fare.policy.FarePolicy;
import wooteco.subway.domain.fare.policy.distance.BasePolicy;

public class Fare {
    private final int fare;

    public Fare(List<FarePolicy> policies, BasePolicy basePolicy) {
        this.fare = calculateTotalFare(policies, basePolicy);
    }

    public int getFare() {
        return fare;
    }

    private int calculateTotalFare(List<FarePolicy> policies, BasePolicy basePolicy) {
        double result = basePolicy.getFare();
        for (FarePolicy policy : policies) {
            result = policy.calculate(result);
        }
        return (int) result;
    }
}
