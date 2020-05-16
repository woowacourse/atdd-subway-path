package wooteco.subway.admin.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EasyExceptionMakerTest {

	@Test
	void validateThrowIAE_WHEN_conditionTrue() {
		assertThatThrownBy(() -> EasyExceptionMaker.validateThrowIAE(true, "예외 발생"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("예외 발생");
	}

	@Test
	void validateThrowIAE_WHEN_conditionFalse() {
		EasyExceptionMaker.validateThrowIAE(false, "예외 안발생");
	}
}