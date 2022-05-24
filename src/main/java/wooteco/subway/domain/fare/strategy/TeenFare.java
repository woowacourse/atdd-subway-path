package wooteco.subway.domain.fare.strategy;

public class TeenFare implements AgeFareStrategy {
    @Override
    public double calculate(int currentFare) {
        return (currentFare - 350) * 0.8;
    }
}
