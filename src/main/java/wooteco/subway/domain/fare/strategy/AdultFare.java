package wooteco.subway.domain.fare.strategy;

public class AdultFare implements AgeStrategy {

    @Override
    public double calculate(int currentFare) {
        return currentFare;
    }
}
