package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountPolicyTest {

	@DisplayName("1250원에 청소년 할인을 적용한다.")
	@Test
	void discountTeen() {
		// given
		DiscountPolicy policy = DiscountPolicy.from(13);

		// when
		int result = policy.apply(1250);

		// then
		assertThat(result).isEqualTo(1070);
	}

	@DisplayName("1250원에 어린이 할인을 적용한다.")
	@Test
	void discountChile() {
		// given
		DiscountPolicy policy = DiscountPolicy.from(12);

		// when
		int result = policy.apply(1250);

		// then
		assertThat(result).isEqualTo(800);
	}

	@DisplayName("성인은 할인이 적용되지 않는다.")
	@Test
	void discountNone() {
		// given
		DiscountPolicy policy = DiscountPolicy.from(19);

		// when
		int result = policy.apply(1250);

		// then
		assertThat(result).isEqualTo(1250);
	}
}
