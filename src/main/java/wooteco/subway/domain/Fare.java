package wooteco.subway.domain;

public class Fare {

	private static final int BASIC_FARE = 1250;
	private static final int INCREASE_FARE = 100;
	private static final int PER_DISTANCE_OVER_TEN = 5;
	private static final int PER_DISTANCE_OVER_FIFTY = 8;

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

	public int getValue() {
		return value;
	}
}
