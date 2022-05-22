package wooteco.subway.domain.fare.strategy;

public class TeenFare implements AgeStrategy {
    @Override
    public double calculate(int currentFare) {
        return (currentFare - 350) * 0.8;
    }
}
