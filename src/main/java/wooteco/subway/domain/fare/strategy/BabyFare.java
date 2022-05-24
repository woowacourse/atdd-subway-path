package wooteco.subway.domain.fare.strategy;

public class BabyFare implements AgeFareStrategy {

    @Override
    public double calculate(int currentFare) {
        return 0;
    }
}
