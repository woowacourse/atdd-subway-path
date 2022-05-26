package wooteco.subway.domain;

import static wooteco.subway.domain.AgeRange.*;

import wooteco.subway.exception.ClientException;

public class Fare {

    private final int distance;
    private final int extraFare;
    private final Long age;

    public Fare(final int distance, final int extraFare, final Long age) {
        validateDistance(distance);
        validateExtraFare(extraFare);
        validateAge(age);
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = age;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new ClientException("[ERROR] 경로 이동 거리는 양수만 가능 합니다.");
        }
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < 0) {
            throw new ClientException("[ERROR] 노선 추가요금은 음수 일 수 없습니다.");
        }
    }

    private void validateAge(Long age) {
        if (age < 0) {
            throw new ClientException("[ERROR] 나이는 음수 일 수 없습니다.");
        }
    }

    public int calculateFare() {
        int fare = DistanceRange.findRange(distance).calcFareByDistance(distance) + extraFare;
        return (int) (fare * findRange(age).getDiscountRate());
    }
}
