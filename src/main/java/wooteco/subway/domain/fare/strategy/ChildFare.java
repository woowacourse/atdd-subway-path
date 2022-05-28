package wooteco.subway.domain.fare.strategy;

public class ChildFare implements AgeFareStrategy {

    @Override
    public double calculate(int currentFare) {
        return (currentFare - 350) * 0.5;
    }
}
