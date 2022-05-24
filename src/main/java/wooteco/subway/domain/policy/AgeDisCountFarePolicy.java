package wooteco.subway.domain.policy;

public class AgeDisCountFarePolicy implements FarePolicy {

    private final int age;

    public AgeDisCountFarePolicy(int age) {
        this.age = age;
    }

    @Override
    public int apply(int fare) {
        if (age < 6) {
            return 0;
        }
        if (age < 13) {
            return (int) (Math.round(fare - 350) * 0.5) + 350;
        }
        if (age < 19) {
            return (int) (Math.round(fare - 350) * 0.8) + 350;
        }
        return fare;
    }
}
