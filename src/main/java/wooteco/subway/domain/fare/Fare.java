package wooteco.subway.domain.fare;

import java.util.ArrayList;
import java.util.List;

public class Fare {
    private final List<FarePolicy> policies;

    public Fare(List<FarePolicy> policies) {
        this.policies = policies;
    }

    public Fare() {
        this(new ArrayList<>());
    }

    public int getFare(int distance) {
        return (int) getConditionAppliedFare(PolicyFactory.createDistance(distance).getFare(distance));
    }

    private double getConditionAppliedFare(int baseFare) {
        double result = baseFare;
        for (FarePolicy policy : policies) {
            result = policy.calculate(result);
        }
        return result;
    }
}
