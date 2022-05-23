package wooteco.subway.domain.fare;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.domain.path.Path;

public class Fare {
    private final List<FarePolicy> policies;

    public Fare(List<FarePolicy> policies) {
        this.policies = policies;
    }

    public Fare() {
        this(new ArrayList<>());
    }

    public static Fare create(Path path, int age) {
        return new Fare(List.of(PolicyFactory.createLineFee(path.getLines()),
                PolicyFactory.createAgeDiscount(age))
        );
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
