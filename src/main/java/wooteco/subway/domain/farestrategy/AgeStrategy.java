package wooteco.subway.domain.farestrategy;

public class AgeStrategy implements FareStrategy {

    private final int age;

    public AgeStrategy(int age) {
        this.age = age;
    }

    @Override
    public long calculate(long fare) {
        AgeStandard age = AgeStandard.from(this.age);
        return age.calculate(fare);
    }
}
