package wooteco.subway.domain.fare;


import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum AgeDisCountPolicy implements DiscountPolicy{

	ADULT(
		age -> age >= Constant.MIN_ADULT_AGE,
		fare -> fare
	),
	TEENAGER(
		age -> age >= Constant.MIN_TEEN_AGE && age < Constant.MIN_ADULT_AGE,
		fare -> (int)(Constant.DEDUCTED_AMOUNT + (fare - Constant.DEDUCTED_AMOUNT) * Constant.TEEN_DISCOUNT_RATE)
	),
	CHILD(
		age -> age >= Constant.MIN_CHILD_AGE && age < Constant.MIN_TEEN_AGE,
		fare -> (int)(Constant.DEDUCTED_AMOUNT + (fare - Constant.DEDUCTED_AMOUNT) * Constant.CHILD_DISCOUNT_RATE)
	)
	;

	private final Predicate<Integer> ageStandard;
	private final IntUnaryOperator discountOperator;

	AgeDisCountPolicy(Predicate<Integer> standard, IntUnaryOperator operator) {
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
	public int apply(int fare) {
		validateFareMinus(fare);
		if (fare < Constant.DEDUCTED_AMOUNT) {
			return fare;
		}
		return discountOperator.applyAsInt(fare);
	}

	private void validateFareMinus(int fare) {
		if (fare < 0) {
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
