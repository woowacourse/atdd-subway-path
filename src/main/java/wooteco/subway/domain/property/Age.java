package wooteco.subway.domain.property;

import wooteco.subway.exception.InvalidRequestException;

import java.util.Arrays;
import java.util.function.Function;

public enum Age {
    BABY(0, 5, (fare) -> fare),
    CHILD(6, 12, (fare) -> (int) Math.ceil((fare - 350) * 0.5)),
    TEENAGER(13, 18, (fare) -> (int) Math.ceil((fare - 350) * 0.2)),
    ADULT(19, 200, (fare) -> 0);

    private final int minAge;
    private final int maxAge;
    private final Function<Integer, Integer> calculateDiscountFare;

    Age(int minAge, int maxAge, Function<Integer, Integer> calculateDiscountFare) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.calculateDiscountFare = calculateDiscountFare;
    }

    public static Age from(Integer age) {
        validEmpty(age);
        validNegative(age);

        return Arrays.stream(values())
                .filter(a -> age >= a.minAge && age <= a.maxAge)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("금액을 산정할 수 없는 나이입니다."));
    }

    public int calculateDiscountFare(int fare) {
        return calculateDiscountFare.apply(fare);
    }

    private static void validNegative(Integer age) {
        if (age < 0) {
            throw new InvalidRequestException("나이는 0 이하가 될 수 없습니다.");
        }
    }

    private static void validEmpty(Integer age) {
        if (age == null) {
            throw new InvalidRequestException("나이는 필수 입력값입니다.");
        }
    }

}
