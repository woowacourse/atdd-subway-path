package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDisCountPolicy implements DiscountPolicy {

	ADULT(
		age -> age >= Constant.MIN_ADULT_AGE,
		fare -> fare
	),
	TEENAGER(
		age -> age >= Constant.MIN_TEEN_AGE && age < Constant.MIN_ADULT_AGE,
		fare ->
			fare.subtract(Constant.DEDUCTED_AMOUNT)
				.multiple(Constant.TEEN_DISCOUNT_RATE)
				.sum(Constant.DEDUCTED_AMOUNT)
	),
	CHILD(
		age -> age >= Constant.MIN_CHILD_AGE && age < Constant.MIN_TEEN_AGE,
		fare ->
			fare.subtract(Constant.DEDUCTED_AMOUNT)
				.multiple(Constant.CHILD_DISCOUNT_RATE)
				.sum(Constant.DEDUCTED_AMOUNT)
	);

	private final Predicate<Integer> ageStandard;
	private final UnaryOperator<Fare> discountOperator;

	AgeDisCountPolicy(Predicate<Integer> standard, UnaryOperator<Fare> operator) {
		this.ageStandard = standard;
		this.discountOperator = operator;
	}

	public static DiscountPolicy from(int age) {
		return Arrays.stream(values())
			.filter(value -> value.ageStandard.test(age))
			.findAny()
			.orElseThrow();
	}

	@Override
	public Fare apply(Fare fare) {
		validateFareMinus(fare);
		if (fare.isShorterThan(Constant.DEDUCTED_AMOUNT)) {
			return fare;
		}
		return discountOperator.apply(fare);
	}

	private void validateFareMinus(Fare fare) {
		if (fare.isShorterThan(0)) {
			throw new IllegalArgumentException("음수는 할인할 수 없습니다.");
		}
	}

	private static class Constant {
		private static final int MIN_ADULT_AGE = 19;
		private static final int MIN_TEEN_AGE = 13;
		private static final int MIN_CHILD_AGE = 6;

		private static final int DEDUCTED_AMOUNT = 350;
		private static final double TEEN_DISCOUNT_RATE = 0.8;
		private static final double CHILD_DISCOUNT_RATE = 0.5;
	}
}
