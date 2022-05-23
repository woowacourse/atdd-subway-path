package wooteco.subway.domain.fare;

import java.util.List;

public class Fare {
    private final List<FarePolicy> policies;

    public Fare(List<FarePolicy> policies) {
        this.policies = policies;
    }

    public int getFare(int baseFare) {
        return (int) getConditionAppliedFare(baseFare);
    }

    private double getConditionAppliedFare(int baseFare) {
        double result = baseFare;
        for (FarePolicy policy : policies) {
            result = policy.calculate(result);
        }
        return result;
    }
}
