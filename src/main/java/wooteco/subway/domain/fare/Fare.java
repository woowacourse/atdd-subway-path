package wooteco.subway.domain.fare;

import static wooteco.subway.domain.fare.FareConstant.*;

public class Fare {

	private final int value;

	private Fare(int value) {
		this.value = value;
	}

	public static Fare of(int distance, int extraFare) {
		validateOverZero(distance);
		validateOverZero(extraFare);
		return new Fare(calculateFare(distance, extraFare));
	}

	private static void validateOverZero(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("0 이상이어야 합니다.");
		}
	}

	private static int calculateFare(int distance, int extraFare) {
		return BASIC_FARE + extraFare + getOverTenFare(distance) + getOverFiftyFare(distance);
	}

	private static int getOverTenFare(int distance) {
		if (distance > 10) {
			return Math.min(800, (int)(Math.ceil((distance - 10) / PER_DISTANCE_OVER_TEN) + 1) * INCREASE_FARE);
		}
		return 0;
	}

	private static int getOverFiftyFare(int distance) {
		if (distance > 50) {
			return (int)(Math.ceil((distance - 50) / PER_DISTANCE_OVER_FIFTY)) * INCREASE_FARE;
		}
		return 0;
	}

	public Fare discountByAge(int age) {
		return new Fare(
			DiscountPolicy.from(age)
				.apply(value)
		);
	}

	public int getValue() {
		return value;
	}
}
