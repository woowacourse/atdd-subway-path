package wooteco.subway.domain.fare;

import java.util.List;
import wooteco.subway.domain.fare.policy.FarePolicy;

public class Fare {
    private final int fare;

    public Fare(List<FarePolicy> policies, int baseFare) {
        this.fare = calculateTotalFare(policies, baseFare);
    }

    public int getFare() {
        return fare;
    }

    private int calculateTotalFare(List<FarePolicy> policies, int baseFare) {
        double result = baseFare;
        for (FarePolicy policy : policies) {
            result = policy.calculate(result);
        }
        return (int) result;
    }
}
