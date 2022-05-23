package wooteco.subway.domain.fare;

import wooteco.subway.exception.SubwayException;

public class FareCondition {

    private final int distance;
    private final int age;
    private final int extraFare;

    public FareCondition(final int distance, final int age, final int extraFare) {
        validateRightAge(age);
        this.distance = distance;
        this.age = age;
        this.extraFare = extraFare;
    }

    private void validateRightAge(int age) {
        if (age < 0) {
            throw new SubwayException("[ERROR] 나이는 양수로 입력하세요");
        }
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
