package wooteco.subway.domain.fare.strategy;

public class AdultFare implements AgeFareStrategy {

    @Override
    public double calculate(int currentFare) {
        return currentFare;
    }
}
