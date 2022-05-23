package wooteco.subway.domain.fare;

import static wooteco.subway.domain.fare.FareConstant.*;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum AgeDisCountPolicy implements DiscountPolicy{

	ADULT(
		age -> age >= MIN_ADULT_AGE,
		fare -> fare
	),
	TEENAGER(
		age -> age >= MIN_TEEN_AGE && age < MIN_ADULT_AGE,
		fare -> (int)(DEDUCTED_AMOUNT + (fare - DEDUCTED_AMOUNT) * TEEN_DISCOUNT_RATE)
	),
	CHILD(
		age -> age >= MIN_CHILD_AGE && age < MIN_TEEN_AGE,
		fare -> (int)(DEDUCTED_AMOUNT + (fare - DEDUCTED_AMOUNT) * CHILD_DISCOUNT_RATE)
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
		if (fare < DEDUCTED_AMOUNT) {
			return fare;
		}
		return discountOperator.applyAsInt(fare);
	}

	private void validateFareMinus(int fare) {
		if (fare < 0) {
			throw new IllegalArgumentException("음수는 할인할 수 없습니다.");
		}
	}
}
